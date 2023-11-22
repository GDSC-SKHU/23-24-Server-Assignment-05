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
  private final TokenProvider tokenProvider; //TokenProvider 주입, JWT인증에 사용됨

  @Override
  public void configure(HttpSecurity http) { // JWT인증을 적용하는 설정
    JwtFilter customFilter = new JwtFilter(tokenProvider); //tokenProvider 사용해서 jwt 토큰을 처리하는 jwtfilter를 새롭게 정의
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
  }
}