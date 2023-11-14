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

@Slf4j // 자바 로깅 api 제공.
@Component
public class TokenProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final String BEARER_TYPE = "Bearer";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분

  private final Key key;

  public TokenProvider(@Value("${jwt.secret}") String secretKey) { // 생성자
    byte[] keyBytes = Decoders.BASE64.decode(secretKey); // secretKey를 BASE64 형식으로 디코딩
    this.key = Keys.hmacShaKeyFor(keyBytes); // 키 값 생성.
  }

  public TokenDto generateTokenDto(Authentication authentication) { // 토큰 생성 메서드.

    String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(",")); // 인증 정보에서 권한 정보를 가져와서 문자열로 바꿈.

    long now = (new Date()).getTime(); // 현재 시간 가져옴.

    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME); // 토큰의 만료 시간을 설정.
    String accessToken = Jwts.builder() // 위 값들로 jwt 생성.
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    return TokenDto.builder() // 토큰 정보를 가진 Dto 반환.
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .build();
  }

  public Authentication getAuthentication(String accessToken) { // 토큰으로부터 인증 정보를 가져오는 메소드

    Claims claims = parseClaims(accessToken); // 토큰에서 claims 추출.

    if (claims.get(AUTHORITIES_KEY) == null) { // 권한 없으면 예외처리.
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()); // 권한 정보를 가저와서 인증 객체를 만듦.

    UserDetails principal = new User(claims.getSubject(), "", authorities); // 유저 객체를 만듦.

    return new UsernamePasswordAuthenticationToken(principal, "", authorities); // 토큰을 반환.
  }

  public boolean validateToken(String token) { // 토큰의 유효성 체크 메서드.
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // 유효한 경우 true 반환.
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
    return false; // 위 경우 catch에 걸리면 false 반환.
  }

  private Claims parseClaims(String accessToken) { // getAuthentication 메서드를 위해, 토큰에서 claims를 파싱하는 메서드.
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}