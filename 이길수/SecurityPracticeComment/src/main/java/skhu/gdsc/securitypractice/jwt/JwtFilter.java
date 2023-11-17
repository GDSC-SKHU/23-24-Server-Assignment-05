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

/*OncePerRequestFilter
Http Request의 한 번의 요청에 대해 한 번만 실행하는 Filter
 */
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;

  /*doFilterInternal
  JWT 토큰을 받고, 내보내는 endpoint 이다.
   */
  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws IOException, ServletException {

    String jwt = resolveToken(request);

    /*hasText()
    문자열 유효성 검증 유틸 메소드(null, 길이가 0, "", " " 이면 false)
    값으로 null을 줘도 NullPointException을 발생시키지 않는다.
     */
    /*validateToken
    토큰 유효성 검사
     */
    /*Authentication
    인증 정보 저장 객체
     */
    /*SecurityContextHolder
    인증된 사용자 정보 관리
     */
    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
      Authentication authentication = tokenProvider.getAuthentication(jwt);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }

  // 토큰 유효성 검증
  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
