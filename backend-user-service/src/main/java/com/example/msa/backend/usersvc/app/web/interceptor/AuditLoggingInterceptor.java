package com.example.msa.backend.usersvc.app.web.interceptor;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.example.msa.common.apinfra.cloud.aws.log.dynamodb.model.Log;
import com.example.msa.common.apinfra.cloud.aws.log.dynamodb.repository.LogRepository;
import com.example.msa.common.apinfra.utils.DateStringUtils;

@Component
public class AuditLoggingInterceptor implements HandlerInterceptor {

  private static final String HEADER_KEY = "X-Amzn-Trace-Id";

  @Autowired LogRepository logRepository;

  /** リクエストヘッダからトレースIDを取得し、取得したIDをDynamoDBに保存する。 */
  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
    Log log =
        Log.builder()
            .userId("1")
            .traceId(getTraceId(request))
            .createdAt(DateStringUtils.now())
            .build();

    logRepository.save(log);
  }

  private String getTraceId(HttpServletRequest request) {
    String header = request.getHeader(HEADER_KEY);
    String[] headerElements = StringUtils.split(header, ":");
    String rootElements =
        Arrays.stream(headerElements).filter(e -> e.startsWith("Root=")).findFirst().get();
    String[] traceIdElement = StringUtils.split(rootElements, "=");
    return traceIdElement[1];
  }
}
