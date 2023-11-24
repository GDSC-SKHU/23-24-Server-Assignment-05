package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/*
* AuthenticationEntryPoint 인터페이스는
* 사용자가 인증되지 않았거나 유효한 인증 정보가 부족해서 요청이 거부된 것을
* httpStatus 401 unauthorized로 보여준다.
* 다시 말해 이 클래스는 accessToken을 발급받지 못한 사용자에 대한 예외를 핸들링한 것이다.
* */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  //AuthenticationEntryPoint의 commence 메소드를 오버라이딩함.
  //응답으로 UNAUTHORIZED라는 에러를 보낸다.
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
          throws IOException {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
