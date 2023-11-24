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
  public PasswordEncoder passwordEncoder() { //비밀번호 암호화
    return new BCryptPasswordEncoder();
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()//토큰방식이므로 csrf disable

            .exceptionHandling()// 인증이 실패하면 jwtAuthenticationEntryPoint실행, 인가가 실패하면 jwtAccessDeniedHandle실행
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션은 사용하지 않으므로 STATELESS

            .and()
            .authorizeHttpRequests()
            .requestMatchers("/login", "/signup/*", "/error").permitAll()//"/login", "/signup/*", "/error"는 인증없이 접근가능
            //인가할 때 오류가 발생해서 "/error"로 매핑되면 401에러가 발생하므로 인증없이 접근 가능하게 해서 403에러가 뜨게 함
            .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")//"/admin"에 접근할 때는 Authority가 "ROLE_ADMIN"이어야 접근 가능
            .anyRequest().authenticated()//다른 모든 접근은 인증 필요

            .and()
            .apply(new JwtSecurityConfig(tokenProvider));//security에 JwtSecurityConfig적용

    return http.build();
  }
}
