package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component //Bean으로 이 클래스의 인스턴스를 생성
public class JwtAccessDeniedHandler implements AccessDeniedHandler { //권한이 없는 사용자가 리소스에 접근할때 사용할 핸들러


  //3가지 파라미터를 받는 handle메서드 요청정보, 응답 정보, 권한이 없는 접근이 감지될 경우 발생하는 예외 객체
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException {
    response.sendError(HttpServletResponse.SC_FORBIDDEN); //클라이언트에게 상태코드403을 전송(접근 권한이 없음을 알려줌)
  }
}