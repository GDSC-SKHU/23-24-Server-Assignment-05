package skhu.gdsc.securitypractice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import skhu.gdsc.securitypractice.jwt.JwtFilter;
import skhu.gdsc.securitypractice.jwt.TokenProvider;

@RequiredArgsConstructor // final 필드를 인자값으로 하는 생성자를 만들어줌
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> { // SecurityConfigurerAdapter를 상속받아서 JwtFilter를 Security 로직에 필터로 등록
  private final TokenProvider tokenProvider; // TokenProvider 주입받음

  @Override
  public void configure(HttpSecurity http) { // JwtFilter를 Security 로직에 필터로 등록
    JwtFilter customFilter = new JwtFilter(tokenProvider); // JwtFilter 생성
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class); // Security 로직에 필터로 등록
  }
}