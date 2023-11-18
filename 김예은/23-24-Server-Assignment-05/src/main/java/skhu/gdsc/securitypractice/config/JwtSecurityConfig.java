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
  private final TokenProvider tokenProvider; // JWT를 생성하고 검증하는데 사용되는 tokenProvider를 선언

  @Override
  public void configure(HttpSecurity http) { // JWT 인증 필터를 추가
    JwtFilter customFilter = new JwtFilter(tokenProvider);

    // security 로직에 JwtFilter 등록
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
  }
}