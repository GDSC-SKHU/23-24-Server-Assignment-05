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
  // JWT 토큰을 처리하기 위한 클래스
  private final TokenProvider tokenProvider;
  // 인증 실패 핸들러
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  // 접근 거부 핸들러
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  // BCryptPasswordEncoder를 사용하여 비밀번호 인코딩을 위한 Bean 정의
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // HTTP 보안 관련 설정을 정의
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable() // CSRF 보호 기능 비활성화

            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) //  인증 실패 시 핸들링을 정의
            .accessDeniedHandler(jwtAccessDeniedHandler) // 접근 거부 시 핸들링을 정의

            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않고, 상태가 없는(stateless) 방식으로 설정

            .and()
            .authorizeHttpRequests()
            .requestMatchers("/login", "/signup/*").permitAll() // 로그인, 회원 가입 경로는 누구나 접근 가능하도록 설정
            .requestMatchers("/admin").hasAuthority("ROLE_ADMIN") // 'admin' 경로는 'ROLE_ADMIN' 권한을 가진 사용자만 접근 가능
            .anyRequest().authenticated() // 그 외의 모든 요청은 인증이 필요함

            .and()
            .apply(new JwtSecurityConfig(tokenProvider)); // JWT에 관한 설정을 추가

    return http.build();
  }
}
