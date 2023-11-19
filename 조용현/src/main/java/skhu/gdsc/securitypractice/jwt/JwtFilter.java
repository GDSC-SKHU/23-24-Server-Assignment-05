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
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws IOException, ServletException {

    String jwt = resolveToken(request);// 토큰을 꺼내서 jwt에 저장

    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {//토큰을 검증하고 Authentication으로 변환한뒤 SecurityContext에 저장
      Authentication authentication = tokenProvider.getAuthentication(jwt);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);//다음 필터 실행
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);//토큰을 꺼내서 리턴
    }
    return null;
  }
}
