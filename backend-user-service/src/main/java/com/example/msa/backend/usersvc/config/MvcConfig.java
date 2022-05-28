package com.example.msa.backend.usersvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.example.msa.common.apinfra.exception.CommonExceptionHandler;

@ComponentScan("com.example.msa.backend.usersvc.app.web")
@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Bean
  CommonExceptionHandler commonExceptionHandler() {
    return new CommonExceptionHandler();
  }
}
