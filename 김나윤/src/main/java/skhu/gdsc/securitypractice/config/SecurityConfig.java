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

  // PasswordEncoder 빈(Spring 객체) 생성
  @Bean
  // BCryptPasswordEncoder: BCrypt 해싱 함수를 사용해 비밀번호 인코딩, 저장소에 있는 비밀번호와 일치 여부 확인
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // SecurityFilterChain 빈 생성
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    /*
    CSRF(Cross Site Reauest Forgery: 사이트간 위조요청) 보호기능 비활성화:
    Rest api는 서버에 인증정보 저장하지 않음(jwt 토큰이용) -> 보호기능 불필요
     */
    http.csrf().disable()

            // 인증,인가 에러시 예외처리
            .exceptionHandling()
            // 유저 인증에 실패했을 경우
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            // 권한이 없을 경우
            .accessDeniedHandler(jwtAccessDeniedHandler)

            .and()
            .sessionManagement()
            // 세션 사용하지 않음
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            // http 요청시
            .authorizeHttpRequests()
            // 모든 유저에게 "/login", "/signup/*" 경로 허용
            .requestMatchers("/login", "/signup/*").permitAll()
            // "ROLE_ADMIN"권한을 가진 유저만 "/admin"경로 접근 가능
            .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
            // 다른 요청은 인증된 유저에게만 허용
            .anyRequest().authenticated()

            .and()
            // JWT 설정 적용
            .apply(new JwtSecurityConfig(tokenProvider));

    return http.build();
  }
}
