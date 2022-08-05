package com.example.msa.frontend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Segment;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.entities.TraceHeader;
import com.example.msa.frontend.domain.ServiceProperties;

@Profile("dev")
@Configuration
public class DevConfig {
  /** バックエンドのマイクロサービスが実行されているホストのURLを定義したプロパティファイルの値を保持するオブジェクト */
  @Autowired ServiceProperties serviceProperties;

  @Bean
  public WebClient userWebClient() {
    return WebClient.builder()
        .baseUrl(serviceProperties.getDns())
        // WebClientに共通して行うフィルタ処理を定義する
        .filter(exchangeFilterFunction())
        .build();
  }

  /**
   * AWSXRayから実行中のセグメント・サブセグメントオブジェクトを素ロッドローカルから取得し、TraceHeaderのインスタンスを、セグメント・サブセグメントの情報から生成する。
   *
   * @return リクエストヘッダを設定した新しいリクエスト
   */
  private ExchangeFilterFunction exchangeFilterFunction() {
    return (clientRequest, nextFilter) -> {
      Segment segment = AWSXRay.getCurrentSegment();
      Subsegment subsegment = AWSXRay.getCurrentSubsegment();
      TraceHeader traceHeader =
          new TraceHeader(
              segment.getTraceId(),
              segment.isSampled() ? subsegment.getId() : null,
              segment.isSampled()
                  ? TraceHeader.SampleDecision.SAMPLED
                  : TraceHeader.SampleDecision.NOT_SAMPLED);
      ClientRequest newClientRequest =
          ClientRequest.from(clientRequest)
              .header(TraceHeader.HEADER_KEY, traceHeader.toString())
              .build();
      return nextFilter.exchange(newClientRequest);
    };
  }
}
