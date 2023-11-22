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

@RequiredArgsConstructor // 클래스 인자 자동생성하는 기능(final 키워드가 붙은것만)
public class JwtFilter extends OncePerRequestFilter { // 요청당 한번만 실행함을 보장하는 기능을 제공함
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider; // 토큰 제공자


  //FilterChain 인터페이스를 통해서 필터링?하고 다음 필터로 요청을 전달함
  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws IOException, ServletException {

    String jwt = resolveToken(request); // 요청을 내부에서 처리하는 형식에 맞춰서 한번 더 검사

    // jwt 문자열이 비어있거나 null인지 한번 더 확인함, jwt토큰이 유효한지 확인하는 과정
    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
      Authentication authentication = tokenProvider.getAuthentication(jwt); // jwt를 사용해서 사용자 정보를 가져옴
      SecurityContextHolder.getContext().setAuthentication(authentication); // authentication를 보안 컨텍스트로 설정, 사용자의 인증 정보를 새로 갱신?하는 역할을 함
    }
    filterChain.doFilter(request, response); //다음 필터에게 요청을 전달
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER); // bearerToken에 Aurthorization 헤더 값을 가져옴

    // StringUtils에 hasToken을 통해서 bearerToken이 빈 문자열, null이 아님을 검사,
    // startsWish를 사용해서 "Bearer " 로 문자열이 시작하는지도 검사
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7); // 7번째 문자열부터 끝까지 반환하는 기능을함
    }
    return null; // 아님널로~
  }
}
