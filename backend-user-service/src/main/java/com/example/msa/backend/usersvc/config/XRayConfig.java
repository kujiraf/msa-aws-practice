package com.example.msa.backend.usersvc.config;

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

@Aspect
@EnableAspectJAutoProxy
@Configuration
public class XRayConfig extends AbstractXRayInterceptor {

  private static final String DYNAMODB_ENDPOINT_EXPORT = ""; // TODO DynamoDBのエンドポイントを追加する
  private static final String DYNAMODB_REGION_EXPORT = ""; // TODO DynamoDBのエンドポイントを追加する

  @Autowired CloudFormationStackResolver cloudFormationStackResolver;

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

  @Bean
  public AWSXRayServletFilter awsXrayServletFilter() {
    return new AWSXRayServletFilter("BackendUserService");
  }

  @Bean
  public FilterRegistrationBean<AWSXRayServletFilter> filterRegistrationBean() {
    return new FilterRegistrationBean<>(awsXrayServletFilter());
  }

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

  @Override
  @Pointcut(
      "@within(com.amazonaws.xray.spring.aop.XRayEnabled) "
          + " && execution(* com.example.msa.backend..*.*(..))")
  protected void xrayEnabledClasses() {}

  @EventListener(ApplicationReadyEvent.class)
  public void onStartup() {
    AWSXRay.endSegment();
  }
}
