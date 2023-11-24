package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component // SecurityConfig에서 권한이 없는 사용자의 접근 시 처리
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException {
    response.sendError(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden 응답 코드를 생성하여 클라이언트에게 전송
  }
}