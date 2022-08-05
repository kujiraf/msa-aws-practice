package com.example.msa.frontend.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.amazonaws.xray.AWSXRay;

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    // デフォルトではAWS X-RayがAP起動時のCloudFormationなどのアクセスも記録しようとする
    // そのため、起動時にセグメントを開始するように、beginSegmentを設定する
    AWSXRay.beginSegment("frontend-webapp-init");
    SpringApplication.run(App.class, args);
  }
}
