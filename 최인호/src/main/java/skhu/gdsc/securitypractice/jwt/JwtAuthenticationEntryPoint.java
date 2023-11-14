package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
          throws IOException {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // HttpServletResponse를 사용하여
                                                             // 클라이언트에게 HTTP 상태 코드 401 (Unauthorized)을 전송.
     }
}
