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

@Aspect
@Configuration
@EnableAspectJAutoProxy
public class XRayConfig extends AbstractXRayInterceptor {

  private static final String DYNAMODB_ENDPOINT_EXPORT = "";
  private static final String DYNAMODB_REGION_EXPORT = "";

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

  @Override
  @Pointcut(
      "@within(com.amazonaws.xray.spring.aop.XRayEnabled)"
          + " && execution(* com.example.msa..*.*(..))")
  protected void xrayEnabledClasses() {}

  @Bean
  public AWSXRayServletFilter awsxRayServletFilter() {
    return new AWSXRayServletFilter("MsaFrontendApp");
  }

  @Bean
  public FilterRegistrationBean<AWSXRayServletFilter> filterRegistrationBean() {
    FilterRegistrationBean<AWSXRayServletFilter> bean =
        new FilterRegistrationBean<>(awsxRayServletFilter());
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
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

  @EventListener(ApplicationReadyEvent.class)
  public void onStartup() {
    AWSXRay.endSegment();
  }
}
