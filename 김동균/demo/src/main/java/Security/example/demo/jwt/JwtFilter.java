package Security.example.demo.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    //
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String jwt = resolveToken(request);  //  요청에서 jwt 토큰 추출 및 저장

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {  // jwt내용이 존재하고, 유효성 검증에 통과하면
            Authentication authentication = tokenProvider.getAuthentication(jwt);  // 인증 객체 authentication 생성
            SecurityContextHolder.getContext().setAuthentication(authentication);  // 인증객체를 보안컨텍스트에 저장
        }
        filterChain.doFilter(request, response);  // 다음 필터로 요청 전달
    }

    //
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {  // Authorization 내용이 있고, 헤더가 Bearer로 시작한다면
            return bearerToken.substring(7);  // JWT 토큰 추출값 봔환(Bearer 값 제외)
        }
        return null;
    }
}
