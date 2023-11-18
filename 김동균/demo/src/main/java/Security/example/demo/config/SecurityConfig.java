package Security.example.demo.config;

import Security.example.demo.jwt.JwtAccessDeniedHandler;
import Security.example.demo.jwt.JwtAuthenticationEntryPoint;
import Security.example.demo.jwt.TokenProvider;
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
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // 비밀번호 암호화 역할을 하는 BCryptPasswordEncoder()를 빈으로 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()  //csrf 비활성화, csrf 공격으로부토 보호하기 위해

                .exceptionHandling()  // 예외처리설정
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)  // 인증되지 않은 사용자가 접근하면 jwtAuthenticationEntryPoint 호출
                .accessDeniedHandler(jwtAccessDeniedHandler)  // 권한이 없은 경우 jwtAccessDeniedHandler 호출

                .and()
                .sessionManagement()  // 세션관리설정
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 세션 관리 정책을 STATELESS로 설정. 세션을 사용하지 않음

                .and()
                .authorizeHttpRequests()  // 요청인가설정
                .requestMatchers("/login", "/signup/*").permitAll()  // 로그인 및 회원가입 요청 항상 허용
                .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")  // 관리자 관련 요청은 ROLE_ADMIN 권한을 가진 사용자만 허용
                .anyRequest().authenticated()  // 나머지 요청 인증된 사용자에게만 허용

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));  // JWT토큰 생성 및 검증

        return http.build();
    }
}
