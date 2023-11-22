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
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    /*
     response.sendError()를 호출하면 response 내부에는 오류가 발생했다는 상태를 저장
     HTTP 응답정보를 처리하는 객체 401 (Unauthorized) 에러 발생
    */
  }
}
