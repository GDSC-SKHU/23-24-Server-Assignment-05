package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

//@Component는 개발자가 직접 작성한 클래스를 Bean으로 등록하기 위한
//어노테이션이다.
//해당 클래스는 jwt으로 인한 액세스가 거부당했을 때를 위한 핸들러클래스다.
//=>다른 말로 하자면, 인증된 사용자가 인가되지 않은 페이지 접근했을 때 처리할 클래스다.
//예외발생시 응답으로 error메세지를 보내준다.
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException {
    response.sendError(HttpServletResponse.SC_FORBIDDEN);
  }
}