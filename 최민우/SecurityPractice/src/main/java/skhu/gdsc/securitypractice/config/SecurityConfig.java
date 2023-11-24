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
    // JWT 토큰을 생성하고 검증하는 데 사용되는, tokenProvider를 생성
    private final TokenProvider tokenProvider;
    // JwtAuthenticationEntryPoint: 인증되지 않은 요청에 대한 처리를 담당
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    // JwtAccessDeniedHandler: 인가 거부에 대한 처리를 담당
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean // 비밀번호를 안전하게 저장하고 인증 시에 비밀번호를 비교
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // 필터 체인은 HTTP 요청에 대해 적용되는 여러 보안 필터들의 체인을 의미하며, 순서대로 필터를 적용하여 보안 기능을 수행
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // CSRF(Cross-Site Request Forgery) 보호 기능을 비활성화하는 메서드 호출

                .exceptionHandling() // 예외 처리
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패 시 처리를 담당
                .accessDeniedHandler(jwtAccessDeniedHandler) // 접근 거부 시 처리를 담당


                .and()
                .sessionManagement() // 세션 관리 설정
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 생성하지 않고 STATELESS 상태로 관리(토큰 기반 인증)

                .and()
                .authorizeHttpRequests() // HTTP 요청에 대한 인가 규칙 설정
                .requestMatchers("/login", "/signup/*").permitAll() // "/login", "/signup/*" 경로에 대한 요청은 모든 사용자에게 허용
                .requestMatchers("/admin").hasAuthority("ROLE_ADMIN") // "/admin" 경로에 대한 요청은 "ROLE_ADMIN" 권한을 가진 사용자만 허용
                .anyRequest().authenticated() // 그 외의 모든 요청은 인증된 사용자에게만 허용

                .and()
                .apply(new JwtSecurityConfig(tokenProvider)); // JwtSecurityConfig를 적용하여, JWT 관련 보안 구성을 설정

        return http.build(); // 설정된 HttpSecurity 객체를 기반으로 SecurityFilterChain을 생성하여 반환
    }
}