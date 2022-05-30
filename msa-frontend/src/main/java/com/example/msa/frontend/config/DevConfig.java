package com.example.msa.frontend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.msa.frontend.domain.ServiceProperties;

@Profile("dev")
@Configuration
public class DevConfig {
  @Autowired ServiceProperties serviceProperties;

  @Bean
  public WebClient userWebClient() {
    return WebClient.builder().baseUrl(serviceProperties.getDns()).build();
  }
}
