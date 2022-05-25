package com.example.msa.frontend.app.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class CustomUserDetails implements UserDetails {

  private static final long serialVersionUID = 1L;
  private final Collection<GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    // ハッシュかも暗号化もせずに処理するパスワードエンコーダのため、noopを付けている。
    return "{noop}test";
  }

  @Override
  public String getUsername() {
    return "test";
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
