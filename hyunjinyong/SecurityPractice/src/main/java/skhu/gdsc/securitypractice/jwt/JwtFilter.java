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


/* jwtfilter는 사용자가 보낸 요청 헤더의 token이 올바르다면,
* 사용자에 대해 인증(authentication)을 해주는 역할의 클래스다.
* */
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;

  /* 요청 헤더의 token이 올바르다면, 해당 토큰으로부터 필요한 정보가
  포함된 Authentication 객체를 생성함.
  SecurityContextHolder.getContext().setAuthentication(authentication);
  위의 부분은 authentication 객체를 securitycontext에 등록하는 코드다.
  * */
  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws IOException, ServletException {

    String jwt = resolveToken(request);

    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
      Authentication authentication = tokenProvider.getAuthentication(jwt);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }

  //사용자가 보낸 요청 헤더에서 authorization 부분을 추출하는 메소드이다.
  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    //token은 null이 없어야 하고, 공백이 아닌 문자로 길이가 0보다 커야 하며
    //Bearer라는 문자열로 시작했을 때, token의 7번째 부분부터 반환한다.
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
