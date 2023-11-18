package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
//인증
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override // HttpServletRequest: 요청 , HttpServletResponse : 응답 , AuthenticationException : 인증 과정에서 발생할 수 있는 예외
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
          throws IOException {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  } // commence 메소드는 클라이언트가 보낸 요청이 인증되지 않았을 때 실행됨.
} // SC_UNAUTHORIZED(응답 코드 401) : HttpServletResponse의 sendError 메소드를 이용하여 HTTP응답 코드 401을 클라이언트에게 반환

//Servlet(서블릿)이란: 동적 웹 페이지를 만들 때 사용되는 자바 기반의 웹 애플리케이션 프로그래밍 기술.
//서블릿은 웹 요청과 응답의 흐름을 간단한 메서드 호출만으로 체계적으로 다룰 수 있게 해준다.

//서블릿의 주요 특징:
/*
1. 클라이언트의 Request에 대해 동적으로 작동하는 웹 어플리케이션 컴포넌트
2. 기존의 정적 웹 프로그램의 문제점을 보완하여 동적인 여러 가지 기능을 제공
3. JAVA의 스레드를 이용하여 동작
4. MVC패턴에서 컨트롤러로 이용됨
5. 컨테이너에서 실행
6. 보안 기능을 적용하기 쉬움
*/