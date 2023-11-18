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
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분

  private final Key key;

  public TokenProvider(@Value("${jwt.secret}") String secretKey) { //생성자.
    byte[] keyBytes = Decoders.BASE64.decode(secretKey); // secret key를 Base64로 디코딩하고 이를 JWT 서명 생성을 위한 Key로 사용.
    this.key = Keys.hmacShaKeyFor(keyBytes); // 키값 생성
  }
  //토큰 발행 메소드
  public TokenDto generateTokenDto(Authentication authentication) {

    //authentic객체에서 권한을 가져와 스트림 형식으로 변환 후, GrantedAuthority타입 객체에서 권한을 가져와 ","로 구분되는 문자열 생성.
    String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    long now = (new Date()).getTime();   // 현재 시간
    //Date타입 객체 생성.
    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME); //토큰 만료 시간
    String accessToken = Jwts.builder() // 빌더를 통해 객체를 만들어줌.
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .setExpiration(accessTokenExpiresIn) // 만료시간
            .signWith(key, SignatureAlgorithm.HS256) // 시크릿 키 설정. 해싱 알고리즘
            .compact();
    //DTO로 변환
    return TokenDto.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .build();
  }
  // 반환형이 Authentication인 메소드
  public Authentication getAuthentication(String accessToken) {

    Claims claims = parseClaims(accessToken); // 메소드 아래에 정의

    if (claims.get(AUTHORITIES_KEY) == null) {  //클레임의 AUTHORITIES_KEY가 null이라면 예외를 발생시킴.
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

    UserDetails principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  public boolean validateToken(String token) {//토큰의 유효성을 판단하는 메소드(예외처리를 통한)
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
  //Payload에 클라이언트에 대한 정보가 담겨있음. 클레임(Claims)를 포함.
  //클레임: 정보의 한 덩어리/ key-value형태이고 3가지 형태로 존재: 1. 등록된 클레임 2. 공개 클레임 3. 비공개 클레임
  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}