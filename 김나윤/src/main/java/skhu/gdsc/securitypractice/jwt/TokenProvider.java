package skhu.gdsc.securitypractice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import skhu.gdsc.securitypractice.dto.TokenDto;

@Slf4j // 로깅 프레임워크에 대한 추상화 라이브러리: 자동으로 log 변수 선언
@Component
public class TokenProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final String BEARER_TYPE = "Bearer";
  // access토큰 만료 시간: 30분
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

  private final Key key;

  // 생성자와 시크릿 키 주입받아 key 인스턴스 생성
  public TokenProvider(@Value("${jwt.secret}") String secretKey) {
    // secretkey값 base64으로 디코딩하여 key 변수에 할당
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  // 인증 정보로 TokenDto 생성 메소드
  public TokenDto generateTokenDto(Authentication authentication) {

    // Authentication에서 현재 유저 권한 정보 추출, 스트림으로 변환
    String authorities = authentication.getAuthorities().stream()
            // 유저 권한을 문자열로 매핑
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    // 현재시간
    long now = (new Date()).getTime();

    // access토큰 만료 시간 설정
    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

    // JWT 토큰 생성
    String accessToken = Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    // TokenDto로 반환
    return TokenDto.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .build();
  }

  // 인증 객체 생성 메소드
  public Authentication getAuthentication(String accessToken) {

    // 주어진 토큰을 파싱하여 claims 추출
    Claims claims = parseClaims(accessToken);

    // 권한 정보가 없는 토큰일 때
    if (claims.get(AUTHORITIES_KEY) == null) {
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    // 인증 객체 생성
    Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

    // 유저 객체 생성
    UserDetails principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  // 토큰 유효성 검증 메소드
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
    }
    return false;
  }

  // claims 추출 메소드
  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    }
    // 토큰이 만료된 경우
    catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}