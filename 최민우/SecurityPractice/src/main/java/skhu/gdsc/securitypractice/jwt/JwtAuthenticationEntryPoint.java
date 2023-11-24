package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint { // AuthenticationEntryPoint 인터페이스를 구현한 클래스

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException { // 인증되지 않은 요청이 발생했을 때 호출
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 요청이 인증되지 않았음을 나타내는 응답(401 - Unauthorized)
    }
}