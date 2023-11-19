package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
  // AccessDeniedHandler 인터페이스의 handle 메소드를 오버라이드
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException {
    // HTTP 응답으로 403 에러를 보냄
    // 사용자가 요청한 리소스에 접근할 권한이 없음을 나타냄
    response.sendError(HttpServletResponse.SC_FORBIDDEN);
  }
}