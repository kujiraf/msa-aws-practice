package com.example.msa.frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

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
        .successHandler(null) // TODO
        .failureUrl("/login")
        .usernameParameter("username")
        .passwordParameter("password")
        .permitAll()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(null) // 認証処理で例外が発生した際にハンドリングする設定
        .and()
        .logout()
        .logoutSuccessUrl("/login")
        .permitAll();
    return http.build();
  }

  // TODO
  //	@Bean
  //	public LoginSuccessHandler loginSuccessHandler() {
  //
  //	}

  @Bean
  AuthenticationEntryPoint authenticationEntryPoint() {
    return null; // TODO
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  protected UserDetailsService userDetailService() {
    //		return new CustomUserDetailService(); TODO
    return null;
  }

  @Bean
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailService()).passwordEncoder(passwordEncoder());
  }
}
