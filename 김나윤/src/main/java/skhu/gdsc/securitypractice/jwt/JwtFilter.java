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
public class JwtFilter extends OncePerRequestFilter {

  // HTTP헤더에서 토큰 추출
  public static final String AUTHORIZATION_HEADER = "Authorization";
  // Bearer: JWT/OAuth에 대한 인증 타입
  public static final String BEARER_PREFIX = "Bearer";

  // TokenProvider 의존성 주입
  private final TokenProvider tokenProvider;

  // HTTP 요청 발생했을 때
  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws IOException, ServletException {

    // 토큰 추출
    String jwt = resolveToken(request);

    // 추출한 토큰의 유효성 검사 후 인증 객체 생성, SecurityContextHolder에 인증정보 설정
    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
      Authentication authentication = tokenProvider.getAuthentication(jwt);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    // 유효하지 않을 경우 다음 필터로 체인 넘김
    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    // HTTP 헤더에서 Authorization 헤더 값 추출
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    // 토큰이 유효, Bearer로 시작할 경우
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      // Bearer 제거 후 실제 토큰 값 반환
      return bearerToken.substring(7);
    }
    // 헤더에 유효한 토큰이 없을 경우
    return null;
  }
}
