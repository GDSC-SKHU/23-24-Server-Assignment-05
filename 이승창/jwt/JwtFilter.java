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
public class JwtFilter extends OncePerRequestFilter { // 각 HTTP 요청 당 한 번씩 필터링을 보장
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;

  @Override  // doFilterInternal: HTTP 요청이 들어올 때 실행,
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws IOException, ServletException {

    String jwt = resolveToken(request);  // 실제 토큰 값 할당 됨.

    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // 토큰 유효성 검사
      Authentication authentication = tokenProvider.getAuthentication(jwt); // 사용자의 principal, credential을 authentication에 저장.
      SecurityContextHolder.getContext().setAuthentication(authentication); // 저장한 authentication을 SecuritycontextHolder에 저장.
    }
    filterChain.doFilter(request, response); // 다음 필터 요청. 응답 넘김
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER); //요청에서 헤더의 Authorization을 찾아 반환
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { // Authorization을 찾아 반환한 변수 bearerToken에서 "bearer "로 시작하는지 확인
      return bearerToken.substring(7); // 만족한다면, 실제 토큰 값을 추출하기 위해 "bearer "을 뺀 진짜 토큰 값을 추출.
    }
    return null;
  }
}
//StringUtils.hasText(): 문자열 유효성 검증 유틸 메소드
//null 인 것,
//길이가 0인 것,
//공백("" or " ")인 것
//이 문자열에 하나라도 포함되었다면 False반환.

//Bearer가 뭘까: token 기반 인증 방식을 사용할 경우
//Authorization : <type> <credentials> 의 구성으로 요청 헤더에 인증 토큰을 보낸다.
//token의 type에는 여러가지가 존재하며, 그 중 JWT를 이용할 경우에는 Bearer type에 해당하여
//'Authorization' : 'Bearer ' + <ACCESS_TOKEN> 로 요청 헤더가 구성된다.