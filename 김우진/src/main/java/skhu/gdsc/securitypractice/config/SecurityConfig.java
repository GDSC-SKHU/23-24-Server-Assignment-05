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

  private final TokenProvider tokenProvider; //토큰 생성
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 인증 실패 처리
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler; // 접근 거부 처리

  @Bean
  public PasswordEncoder passwordEncoder() { // 비밀번호를 암호화하는 역할
    return new BCryptPasswordEncoder(); //구현한 bean반환
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // securityfilterchain 빈을 생성하고 반환함, httpsecurity 객체를 http매개변수로 전달해서 사용함
    http.csrf().disable() // csrf 보호기능 비활성화

            .exceptionHandling() // 예외 처리 핸들러
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // jwt인증 실패한 경우 처리할 인증 진입점을 설정하고
            .accessDeniedHandler(jwtAccessDeniedHandler) //접근이 거부된 경우 처리할 핸들러 설정

            .and()
            .sessionManagement() // 세션 관리
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 생성 정책은 stateless으로 서버에 세션을 유지하지 않는 방식으로 함

            .and()
            .authorizeHttpRequests() // http 요청에 인가 규칙을 설정함
            .requestMatchers("/login", "/signup/*").permitAll() // login, signup에 대한 요청은 인증할 필요 없이 허용함
            .requestMatchers("/admin").hasAuthority("ROLE_ADMIN") // admin에 대한 요청은 role_admin권한을 가지는 사람만 허용함
            .anyRequest().authenticated() //이를 제외한 나머지 기능은 인증된 사용자만 허용함

            .and()
            .apply(new JwtSecurityConfig(tokenProvider)); //jwtsecurityconfig에 이와 같은 사항을 적용함

    return http.build();
  }
}
