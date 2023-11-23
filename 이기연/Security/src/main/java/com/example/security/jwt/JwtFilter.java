package com.example.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    // OncePerRequestFilter를 상속하여 한 번의 요청당 한 번만 실행되는 필터를 구현
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // HTTP 헤더에서 토큰을 추출하기 위한 헤더의 이름
    public static final String BEARER_PREFIX = "Bearer ";
    // Bearer 토큰의 접두사

    private final TokenProvider tokenProvider;
    // 토큰을 생성, 유효성 검증 및 사용자 인증을 처리하는 TokenProvider 객체

    @Override
    // doFilterInternal 메서드를 오버라이드하여 필터 동작을 구현
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String jwt = resolveToken(request);
        // HTTP 요청에서 토큰을 추출

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            // 추출된 토큰이 유효하고 비어있지 않은 경우
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            // 토큰을 사용하여 인증 객체를 생성
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // SecurityContextHolder에 인증 객체를 설정
        }
        filterChain.doFilter(request, response);
        // 다음 필터로 요청을 전달
    }

    private String resolveToken(HttpServletRequest request) {
        // 헤더에서 토큰을 추출하는 메서드
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        // Authorization 헤더에서 토큰 값을 가져옴
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            // 토큰 값이 비어있지 않고 Bearer 접두사로 시작하는 경우
            return bearerToken.substring(7);
            // Bearer 접두사를 제외한 실제 토큰 값을 반환
        }
        return null;
        // 토큰이 없는 경우 null을 반환
    }
}