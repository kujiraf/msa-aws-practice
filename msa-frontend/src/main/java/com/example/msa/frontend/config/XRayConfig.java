package com.example.msa.frontend.config;

import java.io.IOException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.util.ResourceUtils;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.handlers.TracingHandler;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.spring.aop.AbstractXRayInterceptor;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;
import com.example.msa.common.apinfra.cloud.aws.CloudFormationStackResolver;

@Aspect // SpringAOPの機能を利用して、このクラスをアスペクト機能を持つコンポーネントとして定義する。これを付けて@PointCutを有効化する。
@Configuration
@EnableAspectJAutoProxy // SpringAOPの機能を利用するクラスとして宣言する
public class XRayConfig extends AbstractXRayInterceptor {

  private static final String DYNAMODB_ENDPOINT_EXPORT = ""; // TODO DynamoDBのエンドポイントを追加する
  private static final String DYNAMODB_REGION_EXPORT = ""; // TODO DynamoDBのエンドポイントを追加する

  /** CloudFormationのスタック情報を取得するためのユーティリティクラス */
  @Autowired CloudFormationStackResolver cloudFormationStackResolver;

  // X-rayの設定をstatic要素内で定義しているが、内部でTraceIDをMDCを使って保護しているため、マルチスレッドでも問題なく動作する
  static {
    try {
      AWSXRayRecorderBuilder builder =
          AWSXRayRecorderBuilder.standard()
              .withSamplingStrategy(
                  new LocalizedSamplingStrategy(
                      ResourceUtils.getURL("classpath:sampling-rules.json")));
      AWSXRay.setGlobalRecorder(builder.build());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * リクエストにTraceIDを設定するためのサーブレットフィルタの設定を行う.<br>
   * インスタンス生成時に、マネコン上に指定した文字列がサービス名として定義される
   *
   * @return
   */
  @Bean
  public AWSXRayServletFilter awsxRayServletFilter() {
    return new AWSXRayServletFilter("MsaFrontendApp");
  }

  /**
   * awsXrayServletFilterで設定したサーブレットを登録する<br>
   * SpringSecurityと併用した際も、先頭にTraceIDを付与するようにフィルタ適用条件に高い優先度を持たせる。<br>
   * この設定により、SpringSecurityが請け負う処理もトレーシングされる。
   *
   * @return
   */
  @Bean
  public FilterRegistrationBean<AWSXRayServletFilter> filterRegistrationBean() {
    FilterRegistrationBean<AWSXRayServletFilter> bean =
        new FilterRegistrationBean<>(awsxRayServletFilter());
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
  }

  /**
   * トレースログを保存するためのDynamoDB設定を定義する。<br>
   * DynamoDBへのアクセスのトレーシングを有効にする場合、リクエストハンドラにTracingHandlerを設定し、static要素で定義したAWSXRayRecorderを設定する
   *
   * @return
   */
  @Bean
  AmazonDynamoDB amazonDynamoDB() {
    return AmazonDynamoDBAsyncClientBuilder.standard()
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(
                cloudFormationStackResolver.getExportValue(DYNAMODB_ENDPOINT_EXPORT),
                cloudFormationStackResolver.getExportValue(DYNAMODB_REGION_EXPORT)))
        .withRequestHandlers(new TracingHandler(AWSXRay.getGlobalRecorder()))
        .build();
  }

  /**
   * サブセグメントを開始終了するコンポーネントの定義条件を設定する<br>
   * ここでは、@XRayEnabledアノテーションを付与した、com.example.msa配下のパッケージにあるすべてのクラスメソッド実行時を定義している
   */
  @Override
  @Pointcut(
      "@within(com.amazonaws.xray.spring.aop.XRayEnabled)"
          + " && execution(* com.example.msa..*.*(..))")
  protected void xrayEnabledClasses() {}

  /** N,O */
  @EventListener(ApplicationReadyEvent.class)
  public void onStartup() {
    AWSXRay.endSegment();
  }
}
