package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  //  JWT 인증에서 인증되지 않은 요청이 접근했을 때 처리
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
          throws IOException {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    // HTTP 응답 상태 코드 401을 반환하도록 설정. 인증되지 않음
  }
}
