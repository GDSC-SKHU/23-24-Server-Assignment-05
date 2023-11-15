package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component // 스프링이 자체적으로 bean에 등록.
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
          throws IOException { // 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 호출.
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // HttpServletResponse를 사용하여
                                                             // 클라이언트에게 HTTP 상태 코드 401 (Unauthorized)을 전송.
     }
}
