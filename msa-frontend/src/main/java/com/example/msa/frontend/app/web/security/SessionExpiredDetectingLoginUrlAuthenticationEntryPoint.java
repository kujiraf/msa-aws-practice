package com.example.msa.frontend.app.web.security;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/** セッションが無効になった場合に、それを検出しタイムアウト画面に遷移させるためのカスタムクラス */
public class SessionExpiredDetectingLoginUrlAuthenticationEntryPoint
    extends LoginUrlAuthenticationEntryPoint {

  public SessionExpiredDetectingLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
    super(loginFormUrl);
  }

  @Override
  protected String buildRedirectUrlToLoginPage(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) {
    String redirectUrl = super.buildRedirectUrlToLoginPage(request, response, authException);
    if (isRequestSessionInvalid(request)) {
      redirectUrl = "timeout";
    }
    return redirectUrl;
  }

  private boolean isRequestSessionInvalid(HttpServletRequest request) {
    return Objects.nonNull(request.getRequestedSessionId()) && !request.isRequestedSessionIdValid();
  }
}
