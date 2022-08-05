package com.example.msa.frontend.app.web.security;

import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.example.msa.common.apinfra.exception.BusinessException;
import com.example.msa.common.model.UserResource;
import com.example.msa.frontend.domain.service.OrchestrationService;

/**
 * ID/パスワードをプロパティとして定義されているインタフェースorg.springframework.security.core.userdetails.UserDetailsを取得して、検証をするクラス。
 * <br>
 * UserDetailsを取得するクラスは、org.springframework.security.core.userdetails.UserDetailsServiceを実装する必要がある。
 */
@XRayEnabled
@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired MessageSource messageSource;
  @Autowired OrchestrationService orchestrationService;

  /**
   * ユーザ名からユーザの情報をバックエンドから取得し、ユーザの権限を判定する。<br>
   * 判定に基づき、適切な権限を付与したUserDetailsを返却する。<br>
   * SpringSecurityは、このメソッドが返却したUserDtailsを使って検証処理を行う。
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      UserResource userResource = orchestrationService.getUserResource(username);
      List<GrantedAuthority> authorities = null;
      if (userResource.getIsAdmin()) {
        authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
      } else {
        authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
      }
      return CustomUserDetails.builder()
          .userResource(userResource)
          .authorities(authorities)
          .build();
    } catch (BusinessException e) {
      throw new UsernameNotFoundException(
          messageSource.getMessage("BE0001", null, Locale.getDefault()), e);
    }
  }
}
