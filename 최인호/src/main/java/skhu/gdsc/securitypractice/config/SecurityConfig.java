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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  private final TokenProvider tokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  //// BCrypt 알고리즘을 이용한 PasswordEncoder를 생성, 반환

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable() // CSRF를 비활성화.

            .exceptionHandling() // 예외 처리 설정을 시작
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패 시 실행할 EntryPoint를 설정
            .accessDeniedHandler(jwtAccessDeniedHandler)// 접근이 거부될 때 실행할 핸들러를 설정

            .and()
            .sessionManagement() // 세션 관리 설정을 시작
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 세션을 생성하지 않고, STATELESS 모드를 사용하도록 설정

            .and()
            .authorizeHttpRequests()// HTTP 요청에 대한 접근 제어를 설정
            .requestMatchers("/login", "/signup/*").permitAll()// "/login"과 "/signup/*" 경로에 대한 요청은 모두 허용
            .requestMatchers("/admin").hasAuthority("ROLE_ADMIN") // "/admin" 경로에 대한 요청은 "ROLE_ADMIN" 권한이 있는 경우에만 허용
            .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요

            .and()
            .apply(new JwtSecurityConfig(tokenProvider)); // JWT를 이용한 인증 설정을 적용

    return http.build(); // 설정 완료된 SecurityFilterChain을 반환
  }
}
