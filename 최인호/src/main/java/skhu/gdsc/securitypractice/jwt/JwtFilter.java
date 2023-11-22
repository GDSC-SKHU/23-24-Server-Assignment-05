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
public class JwtFilter extends OncePerRequestFilter { // OncePerRequestFilter : 한 번 요청에 한 번만 실행시키는 필터.
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws IOException, ServletException { // HTTP 요청이 있을 때마다 실행되는 메소드 재정의

    String jwt = resolveToken(request); // HTTP 요청으로부터 JWT를 추출.

    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // jwt의 내용이 있고 유효하면
      Authentication authentication = tokenProvider.getAuthentication(jwt); // jwt의 인증 정보를 가져와서
      SecurityContextHolder.getContext().setAuthentication(authentication); // 그걸 SecurityContext에 저장.
    }
    filterChain.doFilter(request, response); // 다음 필터 요청, 응답 넘김.
  }

  private String resolveToken(HttpServletRequest request) { // JWT 추출하는 메서드 정의.
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER); // HTTP 요청 헤더에서 'Authorization' 정보를 가져옴.
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { // 'Authorization' 정보가 존재하고, "Bearer "로 시작한다면
      return bearerToken.substring(7); // "Bearer " 이후의 문자열(JWT)을 반환.
    }
    return null; // 아니면 null 반환.
  }
}
