package com.example.security.config;

import com.example.security.jwt.JwtAccessDeniedHandler;
import com.example.security.jwt.JwtAuthenticationEntryPoint;
import com.example.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    // TokenProvider 객체를 주입
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    // JwtAuthenticationEntryPoint 객체를 주입
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    // JwtAccessDeniedHandler 객체를 주입

    @Bean
    // 비밀번호 인코더를 빈으로 등록
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // SecurityFilterChain을 설정하는 메서드
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // CSRF 보호를 비활성화

                .exceptionHandling()
                // 예외 처리 설정
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                // 인증 진입점을 설정
                .accessDeniedHandler(jwtAccessDeniedHandler)
                // 접근 거부 핸들러를 설정

                .and()
                .sessionManagement()
                // 세션 관리 설정
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 세션 생성 정책을 'STATELESS'로 설정 (세션을 사용하지 않음)

                .and()
                .authorizeHttpRequests()
                // 요청에 대한 인가 설정
                .requestMatchers("/login", "/signup/*").permitAll()
                // "/login", "/signup/*"에 대한 요청은 모두 허용
                .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
                // "/admin"에 대한 요청은 "ROLE_ADMIN" 권한이 있는 사용자만 허용
                .anyRequest().authenticated()
                // 그 외의 모든 요청은 인증된 사용자만 허용

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
        // JwtSecurityConfig를 적용 (JWT 관련 설정)

        return http.build();
        // 설정이 적용된 HttpSecurity를 반환
    }
}