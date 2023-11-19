package skhu.gdsc.securitypractice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import skhu.gdsc.securitypractice.jwt.JwtFilter;
import skhu.gdsc.securitypractice.jwt.TokenProvider;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
  // JWT 인증을 위해 SecurityConfigurerAdapter를 상속

  private final TokenProvider tokenProvider; // JWT 토큰 생성 및 검증하는 객체

  @Override
  public void configure(HttpSecurity http) {
    // HttpSecurity 객체를 매개변수로 받아서 HTTP 요청에 대한 보안 처리
    JwtFilter customFilter = new JwtFilter(tokenProvider); // JWT 토큰을 검증하는 필터인 JwtFilter 객체를 생성하고 TokenProvider 객체와 함께 초기화
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    // customFilter를 UsernamePasswordAuthenticationFilter 이전에 추가
    // JWT 필터가 인증 처리 전에 실행되도록 설정
    // JWT 토큰의 검증과 인증 처리 수행
  }
}