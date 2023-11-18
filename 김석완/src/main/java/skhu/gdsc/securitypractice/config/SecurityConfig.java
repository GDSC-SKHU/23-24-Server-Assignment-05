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

@Configuration //Bean을 등록하기 위한 어노테이션, Bean은 스프링 컨테이너에 의해 관리되는 재사용 가능한 소프트웨어 컴포넌트
@RequiredArgsConstructor
public class SecurityConfig {
  private final TokenProvider tokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  @Bean // 비밀번호 암호화에 사용할 PasswordEncoder 빈 등록
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean //SecurityFilterChain 설정
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable() // csrf보호 비활성화   csrf란 희생자의 권한을 도용하여 특정 기능을 실행하는 것

            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패 시 처리하기 위한 커스텀
            .accessDeniedHandler(jwtAccessDeniedHandler) // 인증은 완료 되었으나 접근 권한이 없을 경우 처리하기 위한 커스텀

            .and()
            .sessionManagement() // 세션 관리 설정
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT를 사용하기 때문에 세션 생성 정책을 STATELESS로 세션을 생성하지 않음
                                                                    // ALWAYS 항상 생성, REQUIRED 스프링 시큐리티가 필요시 생성(기본), NEVER 생성하진 않지만 기존에 존재하면 사용
            .and()
            .authorizeHttpRequests() // 요청 권한 설정
            .requestMatchers("/login", "/signup/*").permitAll() // /login, /signup/* 은 모든 사용자에게 허용
            .requestMatchers("/admin").hasAuthority("ROLE_ADMIN") // /admin 은 ROLE_ADMIN 권한을 가진 사용자에게만 허용
            .anyRequest().authenticated() // 그 외 모든 요청은 인증된 사용자에게만 허용

            .and()
            .apply(new JwtSecurityConfig(tokenProvider)); // JwtSecurityConfig를 적용하여 JWT 기반의 인증 설정

    return http.build(); // 설정된 HttpSecurity 객체를 반환
  }
}
