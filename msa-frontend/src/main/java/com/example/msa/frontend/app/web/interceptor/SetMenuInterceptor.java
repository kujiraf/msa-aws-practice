package com.example.msa.frontend.app.web.interceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.example.msa.frontend.app.model.Menu;
import com.example.msa.frontend.app.web.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetMenuInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    log.info(this.getClass().getName() + ": request :" + request);
    return true;
  }

  /**
   * コントローラ実行後、テンプレートエンジンによるレンダリング前に挟み込む処理を実行する。<br>
   * ここでは、CustomUserDetailsServiceで設定した権限に応じて使用可能なメニューのリストをモデルにマッピングしている。
   */
  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      @Nullable ModelAndView modelAndView)
      throws Exception {
    if (Objects.nonNull(modelAndView) && Objects.isNull(modelAndView.getModel().get("menuList"))) {
      SecurityContext securityContext = SecurityContextHolder.getContext();
      Authentication authentication = securityContext.getAuthentication();
      if (Objects.nonNull(authentication)) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
          List<Menu> list =
              ((CustomUserDetails) principal)
                      .getAuthorities()
                      .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                  ? getAdminMenuList()
                  : getMenuList();
          modelAndView.addObject("menuList", list);
        }
      }
    }
  }

  private List<Menu> getAdminMenuList() {
    return Arrays.asList(Menu.PORTAL, Menu.USER_MANAGEMENT);
  }

  private List<Menu> getMenuList() {
    return Arrays.asList(Menu.PORTAL, Menu.LOGOUT);
  }
}
