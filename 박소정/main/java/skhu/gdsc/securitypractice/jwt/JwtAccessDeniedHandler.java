package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component // 빈으로 등록
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
  // 사용자가 인증은 되었지만 특정 자원에 대한 권한(인가)이 없을 때 처리하는 역할

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException {
    response.sendError(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden 응답을 클라이언트로 전송
  }
}