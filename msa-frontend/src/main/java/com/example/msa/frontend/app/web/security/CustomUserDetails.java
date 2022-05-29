package com.example.msa.frontend.app.web.security;

import java.util.Collection;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.msa.common.model.UserResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class CustomUserDetails implements UserDetails {

  private static final long serialVersionUID = 1L;
  private final UserResource userResource;
  private final Collection<GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return userResource.getCredentialResources().stream()
        .filter(ur -> Objects.equals("PASSWORD", ur.getCredentialType()))
        .findFirst()
        .get()
        .getCredentialKey();
  }

  @Override
  public String getUsername() {
    return userResource.getLoginId();
  }

  @Override
  public boolean isAccountNonExpired() {
    // サンプルとして、常にアカウントの期限を有効で返却する
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // サンプルとして、常にアカウントがロックされていないと返却する
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // サンプルとして、常にアカウントの認証情報を有効で返却する
    return true;
  }

  @Override
  public boolean isEnabled() {
    // // サンプルとして、常にアカウントが有効で返却する
    return true;
  }
}
