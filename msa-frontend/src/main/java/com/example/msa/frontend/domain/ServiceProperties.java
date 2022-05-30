package com.example.msa.frontend.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "service")
public class ServiceProperties {
  private String dns;
}
