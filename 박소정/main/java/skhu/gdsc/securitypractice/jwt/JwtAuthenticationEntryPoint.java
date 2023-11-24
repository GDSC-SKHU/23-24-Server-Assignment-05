package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component // 빈으로 등록
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  // 인증이 실패하거나 인증되지 않은 요청이 들어왔을 때 처리하는 역할

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
          throws IOException {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 클라이언트에게 401 Unauthorized 응답 코드를 전송
  }
}
