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
  private final TokenProvider tokenProvider; // JWT 토큰 생성 및 검증
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // JWT 인증에서 인증되지 않은 요청이 접근했을 때 처리
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler; // JWT 인증에서 접근이 거부된 요청이 접근했을 때 처리

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // BCryptPasswordEncoder를 빈으로 등록하여 비밀번호를 암호화
  }
 /*
  빈으로 등록된 객체는 Spring IoC(Inversion of Control) 컨테이너에 의해 관리되는 객체로 필요한 곳에서 DI(Dependency Injection)를 통해 사용
  다른 빈이나 컴포넌트에서 @Autowired 어노테이션을 사용하여 주입받을 수 있음
  Spring IoC 컨테이너는 자동으로 의존성을 주입하여 객체 간의 관계를 관리
  */

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // Spring Security의 구성 메서드로, HTTP 요청에 대한 보안 처리를 구성
    http.csrf().disable()
            // CSRF(Cross-Site Request Forgery) 공격 방지 기능을 비활성화
            // 인증된 사용자의 권한을 이용하여 악의적인 요청을 실행하거나 사용자의 정보를 탈취하거나 조작하는 등의 악의적인 목적을 가질 수 있음
            .exceptionHandling()// 예외 처리 설정
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증되지 않은 요청이 접근했을 때 처리할 인증 진입점을 설정
            .accessDeniedHandler(jwtAccessDeniedHandler) // 접근이 거부된 요청이 접근했을 때 처리할 핸들러를 설정

            .and()
            .sessionManagement() // 세션 관리 설정
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 생성하지 않고 상태를 유지하지 않는 설정 (상태를 클라이언트 측에서 관리)

            .and()
            .authorizeHttpRequests() // HTTP 요청에 대한 인가 설정을 위한 메서드
            .requestMatchers("/login", "/signup/*").permitAll() // /login, /signup/* 에 대해 인증 없이 접근 허용
            .requestMatchers("/admin").hasAuthority("ROLE_ADMIN") // /admin 에 대해 "ROLE_ADMIN" 권한을 가진 사용자만 접근 허용
            .anyRequest().authenticated() // 나머지 모든 요청은 인증된 사용자만 접근 허용

            .and()
            .apply(new JwtSecurityConfig(tokenProvider));

    return http.build();
  }
}
