package Security.example.demo.config;

import Security.example.demo.jwt.JwtFilter;
import Security.example.demo.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;

    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(tokenProvider);  // JwtFilter 필터 객체 생성
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);  // customerFilter가 UsernamePasswordAuthenticationFilter 이전에 실행되도록 설정
    }
}