package skhu.gdsc.securitypractice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import skhu.gdsc.securitypractice.jwt.JwtFilter;
import skhu.gdsc.securitypractice.jwt.TokenProvider;

/*
@RequiredArgsConstructor
Lombok으로 스프링에서 DI(의존성 주입)의 방법 중에 생성자 주입을 임의의 코드없이 자동으로 설정해줌
초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 줌

SecurityCongifurerAdapter는 <O, B extends SecurityBuilder<O>
재귀적 타입 한정 문접을 활용하여 타입 안정성과 문법 강제의 효과를 가져감

SecurityBuilder는 웹 보안을 구성하는 빈 객체와, 설정 클래스들을 생성하는 역할을 수행
종류로는 WebSecurity, HttpSecurity
SecurityBuilder는 SecurityConfigurer를 포함
인증 및 인가 초기화 작업은 SecurityBuilder 내부에서 SecurityConfigurer를 통해 진행

SecurityConfigurer은 Http 요청과 관련된 보안처리를 담당하는 필터들을 생성하고, 여러 초기화 작업에 관여
 */
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
  private final TokenProvider tokenProvider;

  @Override
  public void configure(HttpSecurity http) {
    JwtFilter customFilter = new JwtFilter(tokenProvider);
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
  }
}