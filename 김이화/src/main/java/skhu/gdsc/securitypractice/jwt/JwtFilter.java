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
  // JWT 토큰 검증 및 인증
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws IOException, ServletException {
    // 요청을 필터링하고 토큰을 검증하여 인증 정보를 설정하는 메서드

    String jwt = resolveToken(request); // JWT 토큰 추출

    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // 토큰 유효성 검사
      Authentication authentication = tokenProvider.getAuthentication(jwt); // 토큰 인증 정보 추출
      SecurityContextHolder.getContext().setAuthentication(authentication); // 추출한 인증 정보를 통해 현재 실행 중인 스레드의 인증 정보 설정
    }
    filterChain.doFilter(request, response); // 나머지 요청을 다음 필터로 전달
  }

  private String resolveToken(HttpServletRequest request) {
    // 요청에서 토큰을 추출하는 메서드
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER); // 요청 헤더에서 Authorization 헤더의 값을 가져옴
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { // 값 유효성 검사 (값이 있는지, Bearer로 시작하는지)
      return bearerToken.substring(7); // Bearer 이후 값 추츌
    }
    return null;
  }
  /*
  토큰은 "Bearer " 접두사를 가지고 있어야 하며, "Bearer " 이후의 텍스트가 토큰 자체
   */
}
