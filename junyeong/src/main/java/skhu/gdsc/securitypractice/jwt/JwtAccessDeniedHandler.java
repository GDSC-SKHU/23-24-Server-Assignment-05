package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler { // JwtAccessDeniedHandler 클래스는 인증된 사용자가 리소스에 접근할 때, 권한이 없는 경우 403 Forbidden 에러를 리턴
  @Override // AccessDeniedHandler 인터페이스의 handle 메소드를 오버라이딩
  public void handle(HttpServletRequest request, HttpServletResponse response, // handle 메소드는 권한이 없는 경우 403 Forbidden 에러를 리턴
                     AccessDeniedException accessDeniedException) throws IOException { // handle 메소드는 권한이 없는 경우 403 Forbidden 에러를 리턴
    response.sendError(HttpServletResponse.SC_FORBIDDEN); // 권한이 없는 경우 403 Forbidden 에러를 리턴
  }
}