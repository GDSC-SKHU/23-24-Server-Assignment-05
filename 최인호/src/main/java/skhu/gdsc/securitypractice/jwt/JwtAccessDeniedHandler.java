package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component // 스프링이 자체적으로 bean에 등록.
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException {
    response.sendError(HttpServletResponse.SC_FORBIDDEN); // 접근이 거절 당했을 때 호출.
                                                          // HttpServletResponse를 사용해서
                                                          // 클라이언트에 403 에러 메세지를 전송한다.
                                                          // 접근 권한이 없는 경우 호출하려고 만든 메서드.
  }
}