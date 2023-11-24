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

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter { // OncePerRequestFilter는 한 요청에 대해 한 번만 실행되도록 함
  public static final String AUTHORIZATION_HEADER = "Authorization"; // HTTP헤더에서 토큰을 찾기 위한 헤더 이름
  public static final String BEARER_PREFIX = "Bearer "; // 토큰 문자열 앞에 붙는 Bearer 접두사

  private final TokenProvider tokenProvider; // 실제 토큰 관리 및 검증을 담당

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws IOException, ServletException {

    String jwt = resolveToken(request); // HTTP 요청으로부터 토큰을 추출

    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // 추출된 토큰이 유효하면
      Authentication authentication = tokenProvider.getAuthentication(jwt); // 해당 토큰을 사용하여 인증 객체 생성
      SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContextHolder에 인증 객체 설정
    }
    filterChain.doFilter(request, response); // 다음 필터로 전달
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER); // Authorization 헤더에서 토큰 값 추출
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { // 토큰 값이 있고 bearer 접두사로 시작하는 경우
      return bearerToken.substring(7); // bearer 접두사를 제외한 실제 토큰 값 반환
    }
    return null;
  }
}
