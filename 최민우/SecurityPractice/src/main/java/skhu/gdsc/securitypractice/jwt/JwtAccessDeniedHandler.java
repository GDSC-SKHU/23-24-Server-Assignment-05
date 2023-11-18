package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler { // AccessDeniedHandler 인터페이스를 구현한 클래스
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException { // 접근 거부가 발생했을 때 호출
        response.sendError(HttpServletResponse.SC_FORBIDDEN); // 해당 요청에 대한 권한이 없음을 나타내는 응답(403 - Forbidden)
    }
}