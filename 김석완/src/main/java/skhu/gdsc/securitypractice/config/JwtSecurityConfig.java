package skhu.gdsc.securitypractice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import skhu.gdsc.securitypractice.jwt.JwtFilter;
import skhu.gdsc.securitypractice.jwt.TokenProvider;

@RequiredArgsConstructor  //생성자 주입을 자동으로 해주는 어노테이션
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
  private final TokenProvider tokenProvider;

  @Override
  public void configure(HttpSecurity http) {
    JwtFilter customFilter = new JwtFilter(tokenProvider); // JwtFilter 객체 생성 및 tokenProvider 주입
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class); // UsernamePasswordAuthenticationFilterFilter.class 앞에 customFilter 추가
  }
}