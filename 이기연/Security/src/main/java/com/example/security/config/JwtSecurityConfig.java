package com.example.security.config;
import com.example.security.jwt.JwtFilter;
import com.example.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor //생성자 주입을 자동으로 생성
public class  JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;
    //// JwtSecurityConfig 클래스의 생성자에서 TokenProvider를 주입
    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        //// JwtFilter 객체를 생성하고 TokenProvider를 주입하여 초기화
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        // // HttpSecurity에 customFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
    }
}