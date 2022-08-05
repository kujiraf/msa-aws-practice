package com.example.msa.backend.usersvc.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.amazonaws.xray.AWSXRay;

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    AWSXRay.beginSegment("backend-user-service-init");
    SpringApplication.run(App.class, args);
  }
}
