package com.example.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
// 스프링 빈으로 등록되는 컴포넌트
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    // AccessDeniedHandler 인터페이스의 메서드를 구현
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        // HTTP 응답에 403 Forbidden 에러를 설정하여 접근 거부 상황을 처리
    }
}
