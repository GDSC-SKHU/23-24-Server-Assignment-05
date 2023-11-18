package skhu.gdsc.securitypractice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import skhu.gdsc.securitypractice.jwt.JwtFilter;
import skhu.gdsc.securitypractice.jwt.TokenProvider;

// Spring Security에서 JWT(JSON Web Token)를 사용하여 인증하는 방법
// Spring Security는 HTTP 요청이 도착했을 때, JwtFilter를 거쳐 JWT의 유효성을 검사하고, 유효한 경우 해당 요청을 처리하기 위해 필터 체인을 진행
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> { // 상속
    // JWT 토큰을 생성하고 검증하는 데 사용되는, tokenProvider를 생성
    private final TokenProvider tokenProvider;

    @Override
    public void configure(HttpSecurity http) {
        // JwtFilter 인스턴스를 생성하고, tokenProvider를 주입하여 초기화
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        // HttpSecurity에 JwtFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}