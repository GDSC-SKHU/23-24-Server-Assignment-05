package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


  //마찬가지로 요청정보, 응답정보를 받아오는 HttpServlet을 사용하고 AuthenticationException은 사용자 인증 정보가 올바르지 않을때 예외를 터트림
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
          throws IOException {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED); //401 상태코드 작성, 인증되지 않은 상태에서 시스템에 접근하면 401상태코드를 반환하는것임
  }
}
