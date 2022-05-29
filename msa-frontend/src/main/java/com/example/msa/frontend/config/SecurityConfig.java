package com.example.msa.frontend.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import com.example.msa.frontend.app.web.security.CustomUserDetailsService;
import com.example.msa.frontend.app.web.security.LoginSuccessHandler;
import com.example.msa.frontend.app.web.security.SessionExpiredDetectingLoginUrlAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    // 静的リソースに対するリクエストを対象外とする設定
    return (web) -> web.ignoring().antMatchers("/static/**");
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // antMatchers().permitAll()でチェック対象外にしている
    http.authorizeRequests()
        .antMatchers("/favicon.ico")
        .permitAll()
        .antMatchers("/webjars/**")
        .permitAll()
        .antMatchers("/static/**")
        .permitAll()
        .antMatchers("/timeout")
        .permitAll()
        // これ以降は全て認証が必要にしている
        .anyRequest()
        .authenticated()
        .and()
        .csrf()
        .disable() // 便宜上、CSRFトークン送信必須設定をオフにしている
        .formLogin()
        .loginProcessingUrl("/authenticate")
        .loginPage("/login")
        .successHandler(loginSuccessHandler())
        .failureUrl("/login")
        .usernameParameter("username")
        .passwordParameter("password")
        .permitAll()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint()) // 認証処理で例外が発生した際にハンドリングする設定
        .and()
        .logout()
        .logoutSuccessUrl("/login")
        .permitAll();
    return http.build();
  }

  @Bean
  public LoginSuccessHandler loginSuccessHandler() {
    return new LoginSuccessHandler();
  }

  @Bean
  AuthenticationEntryPoint authenticationEntryPoint() {
    return new SessionExpiredDetectingLoginUrlAuthenticationEntryPoint("/login");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    //    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
    encoders.put("bcrypt", new BCryptPasswordEncoder());
    PasswordEncoder pe = new DelegatingPasswordEncoder("pbkdf2", encoders);
    return pe;
  }

  @Bean
  protected UserDetailsService userDetailService() {
    return new CustomUserDetailsService();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(userDetailService());
    return new ProviderManager(provider);
  }
}
