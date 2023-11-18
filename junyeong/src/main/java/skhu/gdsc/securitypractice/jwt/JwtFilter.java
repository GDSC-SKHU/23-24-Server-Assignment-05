package skhu.gdsc.securitypractice.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 생성
public class JwtFilter extends OncePerRequestFilter { // JwtFilter 클래스는 Request로 들어오는 Jwt Token의 유효성을 검증하는 역할을 수행
  public static final String AUTHORIZATION_HEADER = "Authorization"; // AUTHORIZATION_HEADER는 "Authorization" 문자열을 저장
  public static final String BEARER_PREFIX = "Bearer "; // BEARER_PREFIX는 "Bearer " 문자열을 저장

  private final TokenProvider tokenProvider; // TokenProvider 주입받음

  @Override // doFilterInternal 메소드를 오버라이딩
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) // doFilterInternal 메소드는 Request로 들어오는 Jwt Token의 유효성을 검증
          throws IOException, ServletException { // doFilterInternal 메소드는 Request로 들어오는 Jwt Token의 유효성을 검증

    String jwt = resolveToken(request); // Request의 Header에서 token 값을 가져옴

    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // Jwt Token의 유효성을 검증
      Authentication authentication = tokenProvider.getAuthentication(jwt);// Jwt Token에서 Authentication 객체를 가져옴
      SecurityContextHolder.getContext().setAuthentication(authentication);// SecurityContext에 Authentication 객체를 저장
    }
    filterChain.doFilter(request, response);// doFilter 메소드를 호출하여 다음 필터로 넘어감
  }

  private String resolveToken(HttpServletRequest request) {// Request의 Header에서 token 값을 가져옴
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);// Request의 Header에서 "Authorization" 값을 가져옴
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) // "Authorization" 값이 존재하고, "Bearer " 문자열로 시작하는 경우
       {
      return bearerToken.substring(7); // "Bearer " 문자열을 제외한 나머지 문자열을 반환
    }
    return null; // "Bearer " 문자열을 제외한 나머지 문자열을 반환
  }
}
