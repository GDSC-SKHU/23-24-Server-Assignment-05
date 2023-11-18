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
public class SecurityConfig { // SecurityConfig 클래스는 Spring Security의 설정을 담당하는 클래스
  private final TokenProvider tokenProvider; // TokenProvider 주입받음
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // JwtAuthenticationEntryPoint 주입받음
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler; // JwtAccessDeniedHandler 주입받음

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  } // PasswordEncoder를 Bean으로 등록


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // SecurityFilterChain을 Bean으로 등록
    http.csrf().disable() // csrf 설정을 비활성화

            .exceptionHandling() // 예외처리를 진행할 때, JwtAuthenticationEntryPoint와 JwtAccessDeniedHandler를 추가
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증되지 않은 사용자가 리소스에 접근할 때, 401 Unauthorized 에러를 리턴
            .accessDeniedHandler(jwtAccessDeniedHandler) // 인증된 사용자가 리소스에 접근할 때, 권한이 없는 경우 403 Forbidden 에러를 리턴

            .and() // 로그인 설정을 진행
            .sessionManagement() // 세션 설정을 진행
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않기 때문에 세션 생성 전략을 NullSessionCreationPolicy로 설정

            .and() // 로그인 설정을 진행
            .authorizeHttpRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근제한 설정을 진행
            .requestMatchers("/login", "/signup/*").permitAll() // 로그인, 회원가입 API는 누구나 접근 가능
            .requestMatchers("/admin").hasAuthority("ROLE_ADMIN") // admin API는 ROLE_ADMIN 권한을 가진 사람만 접근 가능
            .anyRequest().authenticated() // 나머지 API는 모두 인증된 회원만 접근 가능

            .and() // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig 클래스를 적용
            .apply(new JwtSecurityConfig(tokenProvider)); // JwtFilter를 Security 로직에 필터로 등록

    return http.build(); // SecurityFilterChain을 반환
  }
}
