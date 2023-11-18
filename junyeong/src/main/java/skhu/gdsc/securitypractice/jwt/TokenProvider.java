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

@Slf4j // log 사용을 위한 lombok 어노테이션
@Component
public class TokenProvider { // TokenProvider 클래스는 Jwt Token을 생성하고, 유효성을 검증하는 역할을 수행

  private static final String AUTHORITIES_KEY = "auth"; // AUTHORITIES_KEY는 "auth" 문자열을 저장
  private static final String BEARER_TYPE = "Bearer"; // BEARER_TYPE는 "Bearer" 문자열을 저장
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분

  private final Key key; // key 주입받음

  public TokenProvider(@Value("${jwt.secret}") String secretKey) { // TokenProvider 클래스는 Jwt Token을 생성하고, 유효성을 검증하는 역할을 수행
    byte[] keyBytes = Decoders.BASE64.decode(secretKey); // secretKey를 Base64 Decode하여 keyBytes에 저장
    this.key = Keys.hmacShaKeyFor(keyBytes); // keyBytes를 key로 변환
  }

  public TokenDto generateTokenDto(Authentication authentication) { // Jwt Token을 생성

    String authorities = authentication.getAuthorities().stream() // Authentication 객체에서 권한 정보를 가져옴
            .map(GrantedAuthority::getAuthority) // 권한 정보를 가져와서
            .collect(Collectors.joining(",")); // ","로 구분하여 문자열로 변환

    long now = (new Date()).getTime(); // 현재 시간을 가져옴

    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME); // 현재 시간에 30분을 더한 시간을 가져옴
    String accessToken = Jwts.builder() // Jwt Token을 생성
            .setSubject(authentication.getName())// Jwt Token에 이름을 저장
            .claim(AUTHORITIES_KEY, authorities) // Jwt Token에 권한 정보를 저장
            .setExpiration(accessTokenExpiresIn) // Jwt Token에 만료 시간을 저장
            .signWith(key, SignatureAlgorithm.HS256) // Jwt Token에 서명을 저장
            .compact(); // Jwt Token을 생성

    return TokenDto.builder() // TokenDto 객체를 생성
            .grantType(BEARER_TYPE) // TokenDto 객체에 "Bearer" 문자열을 저장
            .accessToken(accessToken) // TokenDto 객체에 accessToken을 저장
            .build(); // TokenDto 객체를 생성
  }

  public Authentication getAuthentication(String accessToken) { // Jwt Token에서 Authentication 객체를 가져옴

    Claims claims = parseClaims(accessToken); // Jwt Token에서 권한 정보를 가져옴

    if (claims.get(AUTHORITIES_KEY) == null) { // 권한 정보가 없는 토큰인 경우
      throw new RuntimeException("권한 정보가 없는 토큰입니다."); // 예외 발생
    }

    Collection<? extends GrantedAuthority> authorities = // 권한 정보를 이용하여
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")) // ","로 구분하여
                    .map(SimpleGrantedAuthority::new) // SimpleGrantedAuthority 객체로 변환
                    .collect(Collectors.toList()); // List로 변환

    UserDetails principal = new User(claims.getSubject(), "", authorities); // UserDetails 객체를 생성

    return new UsernamePasswordAuthenticationToken(principal, "", authorities); // UsernamePasswordAuthenticationToken 객체를 생성
  }

  public boolean validateToken(String token) { // Jwt Token의 유효성을 검증
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // Jwt Token의 서명을 검증
      return true;
    } catch (SecurityException | MalformedJwtException e) { // Jwt Token의 서명이 잘못된 경우
      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {// Jwt Token의 만료 시간이 지난 경우
      log.info("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {// 지원되지 않는 Jwt Token인 경우
      log.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) { // Jwt Token이 잘못된 경우
      log.info("JWT 토큰이 잘못되었습니다.");
    }
    return false; // Jwt Token의 유효성을 검증
  }

  private Claims parseClaims(String accessToken) { // Jwt Token에서 권한 정보를 가져옴
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody(); //
    } catch (ExpiredJwtException e) { // Jwt Token의 만료 시간이 지난 경우
      return e.getClaims(); // Jwt Token에서 권한 정보를 가져옴
    }
  }
}