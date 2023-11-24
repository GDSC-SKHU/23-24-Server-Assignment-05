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
public class JwtFilter extends OncePerRequestFilter { // HTTP 요청을 필터링하여 JWT 토큰을 추출하고 검증한 후, 이를 기반으로 사용자의 인증 정보를 설정하는 역할
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws IOException, ServletException {

    String jwt = resolveToken(request); // HTTP 요청에서 JWT 토큰 추출

    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // // 추출한 JWT 토큰이 유효하면 해당 토큰으로부터 사용자의 인증 정보를 가져와 SecurityContext에 설정
      Authentication authentication = tokenProvider.getAuthentication(jwt);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response); // 다음 필터로 요청 전달
  }

  private String resolveToken(HttpServletRequest request) { // HTTP 요청에서 JWT 토큰을 추출하는 메서드
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }
}

/*
* 필터의 흐름
* HTTP 요청 -> WAS -> 필터들 -> (디스패처) 서블릿 -> 컨트롤러
*
* 필터는 dispatcherServlet 이전에 호출
* 필터는 체인 구성, 자유롭게 추가 가능
* 필터에서 적절하지 않은 요청이라고 판단하면 dispatcherServlet을 호출하지 않고 요청을 끝냄.
*
* */