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
  private final TokenProvider tokenProvider; // JWT를 생성하고 검증하는데 사용되는 TokenProvider를 선언

  @Override
  public void configure(HttpSecurity http) { // JWT 인증 필터를 추가하는 작업.
    JwtFilter customFilter = new JwtFilter(tokenProvider); // jwtFilter 객체를 만들어서
    // UsernamePasswordAuthenticationFilter를 실행하기 전에 JwtFilter가 실행되도록 설정
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
  }
}