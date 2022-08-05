package com.example.msa.frontend.app.web.interceptor;

import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Segment;
import com.example.msa.common.apinfra.cloud.aws.log.dynamodb.model.Log;
import com.example.msa.common.apinfra.cloud.aws.log.dynamodb.repository.LogRepository;
import com.example.msa.common.apinfra.utils.DateStringUtils;
import com.example.msa.frontend.app.web.security.CustomUserDetails;

public class AuditLoggingInterceptor implements HandlerInterceptor {

  @Autowired LogRepository logRepository;

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
    String userId = "0";
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (Objects.nonNull(authentication)) {
      Object principal = authentication.getPrincipal();
      if (principal instanceof CustomUserDetails) {
        userId = ((CustomUserDetails) principal).getUserResource().getUserId();
      }
    }
    Log log =
        Log.builder().userId(userId).createdAt(DateStringUtils.now()).traceId(getTraceId()).build();
    logRepository.save(log);
  }

  private String getTraceId() {
    Optional<Segment> segment = AWSXRay.getCurrentSegmentOptional();
    if (segment.isPresent()) {
      return segment.get().getTraceId().toString();
    }
    return null;
  }
}
