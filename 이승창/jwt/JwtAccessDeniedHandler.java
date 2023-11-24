package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component // 클래스 레벨에서 선언함으로써 스프링이 런타임시에 컴포넌트스캔을 하여 자동으로 빈을 찾고 등록하는 어노테이션
public class JwtAccessDeniedHandler implements AccessDeniedHandler { // AccessDeniedHandler는 스프링 시큐리티에서 접근 거부 시 어떻게 처리할 지를 정의하는 인터페이스.
  @Override // handle메소드: 접근이 거부됐을 때 호출.
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException {
    response.sendError(HttpServletResponse.SC_FORBIDDEN); // HttpServletResponse를 사용하여 SC_FORBIDDEN(403 Forbidden)을 클라에게 보냄.
  } // 이 코드는 클라이언트가 해당 리로스에 대한 접근이 거부됐음을 알려주는 것.
}
// @Bean : 메소드 레벨에서 선언하며, 반환되는 객체(인스턴스)를 개발자가 수동으로 빈으로 등록하는 어노테이션
//bean(빈)은 스프링 컨테이너에 의해 관리되는 재사용 가능한 소프트웨어 컴포넌트
//즉, 스프링 컨테이너가 관리하는 자바 객체, 하나 이상의 빈을 관리한다.
// 빈은 인스턴스화된 객체를 의미한다.
//웹브라우저로부터 Servlet요청을 받으면 요청을 받을 때 전달 받은 정보를 HttpServletRequest객체를 생성해 저장
//웹브라우저에 응답을 돌려줄 HttpServletResponse 객체를 생성(비어있는 객체)
//생성된 HttpServletRequest(정보가 저장된)와 HttpServletResponse(비어있는)를 Servlet에게 전달.

// HttpServletRequest: -Http프로토콜의 request 정보를 서블릿에게 전달하기 위한 목적으로 사용.
//                     -Header정보, Parameter, Cookie, URI, URL 등의 정보를 읽어들이는 메소드를 가진 클래스
//                     -Body의 스트림을 읽어들이는 메소드를 가지고 있음.

// HttpServletResponse: Servlet은 HttpServletResponse객체에 ContentType,응답코드,응답메세지등을 담아서 전송함.
