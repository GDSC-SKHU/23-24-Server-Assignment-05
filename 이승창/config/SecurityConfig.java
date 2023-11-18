package skhu.gdsc.securitypractice.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import skhu.gdsc.securitypractice.jwt.JwtAccessDeniedHandler;
import skhu.gdsc.securitypractice.jwt.JwtAuthenticationEntryPoint;
import skhu.gdsc.securitypractice.jwt.TokenProvider;

@Configuration  // 빈등록(IoC관리)
@RequiredArgsConstructor
public class SecurityConfig {
  private final TokenProvider tokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean  // 보안 설정
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable() // 세션을 사용하지 않고 JWT 토큰을 활용하여 진행, csrf토큰검사를 비활성화

            .exceptionHandling() // 예외 처리 구성
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)// 요청이 인증되지 않았을 떄
            .accessDeniedHandler(jwtAccessDeniedHandler)// 접근이 거부됐을 때

            .and()
            .sessionManagement()//세션 관리
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션 정책: 스프링 시큐리티가 생성하지도 않고 존재해도 사용하지 않음

            .and()
            .authorizeHttpRequests()//HTTP 요청에 대한 인가 설정
            .requestMatchers("/login", "/signup/*").permitAll()//'/login, /signup/*' 경로에 대해서는 모든 사용자에게 허용.
            .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")//'/admin' 경로에 접근하는 사용자는 "ROLE_ADMIN" 권한을 가진 사용자로 설정
            .anyRequest().authenticated()//나머지 모든 요청에 대해서는 인증된 사용자만 허용

            .and()
            .apply(new JwtSecurityConfig(tokenProvider));//JwtSecurityConfig를 적용. 이 구성은 tokenProvider를 사용하여 JWT 토큰의 보안 구성을 설정.

    return http.build();
  }
}
