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
  private final TokenProvider tokenProvider;

  @Override
  // http에 JWT필터 추가
  public void configure(HttpSecurity http) {
    // JwtFilter 클래스 생성자에 TokenProvider 주입
    JwtFilter customFilter = new JwtFilter(tokenProvider);
    // UsernmePasswordAuthenticationFilter 앞에 customFilter 실행하도록 설정
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
  }
}