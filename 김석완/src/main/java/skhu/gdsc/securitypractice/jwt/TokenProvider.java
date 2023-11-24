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

@Slf4j
@Component
public class TokenProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final String BEARER_TYPE = "Bearer";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분 ( 만료 시간 )

  private final Key key;

  public TokenProvider(@Value("${jwt.secret}") String secretKey) { // JWT 서명을 위한 비밀 키
    byte[] keyBytes = Decoders.BASE64.decode(secretKey); // secretKey를 base64로 디코딩하여 keyBytes에 저장
    this.key = Keys.hmacShaKeyFor(keyBytes); // keyBytes의 HMAC SHA키 생성
  }

  public TokenDto generateTokenDto(Authentication authentication) { //TokenDto 반환 메서드

    String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    long now = (new Date()).getTime(); // now에 현재 시간 저장

    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME); // 만료 시간 = 현재 + 30분
    String accessToken = Jwts.builder() // jwt 토큰 생성
            .setSubject(authentication.getName()) // 이름 반환
            .claim(AUTHORITIES_KEY, authorities) // 토큰의 클레임에 사용자 권한 정보 추가
            .setExpiration(accessTokenExpiresIn) // 만료 시간 설정
            .signWith(key, SignatureAlgorithm.HS256) // 서명에 HS256 알고리즘 사용
            .compact(); // 문자열로 직렬화

    return TokenDto.builder()
            .grantType(BEARER_TYPE) // 토큰의 발급 유형
            .accessToken(accessToken) // accessToken 설정
            .build();
  }

  public Authentication getAuthentication(String accessToken) { // Authentication 추출하는 메서

    Claims claims = parseClaims(accessToken); // jwt 토큰을 파싱하여 클레임 저장

    if (claims.get(AUTHORITIES_KEY) == null) { // 권한정보가 없으면 예외 발생
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")) // 권한 정보를 ,로 구분하여 저장
                    .map(SimpleGrantedAuthority::new) // 각 권한 문자열을 SimpleGrantedAuthority객체로 변환
                    .collect(Collectors.toList()); // 컬렉션 형태로 저장

    UserDetails principal = new User(claims.getSubject(), "", authorities); // UserDetails 객체 생성

    return new UsernamePasswordAuthenticationToken(principal, "", authorities); // UsernamePasswordAuthenticationToken 객체 생성
  }

  public boolean validateToken(String token) { // 토큰 검사
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // 토큰 검사
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

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody(); // 클레임 정보 반환
    } catch (ExpiredJwtException e) { // 토큰 시간 만료 시 클레임 정보 반환
      return e.getClaims();
    }
  }
}