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
import com.example.msa.common.apinfra.exception.BusinessException;
import com.example.msa.common.model.UserResource;
import com.example.msa.frontend.domain.service.OrchestrationService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired MessageSource messageSource;
  @Autowired OrchestrationService orchestrationService;

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
