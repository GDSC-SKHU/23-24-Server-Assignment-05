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
public class TokenProvider { //JWT 토큰 생성 및 검증

  private static final String AUTHORITIES_KEY = "auth";
  private static final String BEARER_TYPE = "Bearer";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분

  private final Key key;

  public TokenProvider(@Value("${jwt.secret}") String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public TokenDto generateTokenDto(Authentication authentication) {
    // 인증 정보를 기반으로 JWT 토큰을 생성
    String authorities = authentication.getAuthorities().stream() // authentication 객체에서 권한 정보를 스트림으로 가져옴
            .map(GrantedAuthority::getAuthority) // GrantedAuthority 인터페이스의 getAuthority 메서드 호출하여 권한 정보 추출
            .collect(Collectors.joining(",")); // 추출한 권한 정보를 쉼표로 구분된 문자열로 변환 (구분자 : 쉼표)
    // 인증정보에서 권한을 추출하여 쉼표로 구분된 문자열로 변환

    long now = (new Date()).getTime(); // 현재 시간

    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME); // 현재 시간 + 유효 기간 -> 엑세스 토큰의 만료 시간 설정
    String accessToken = Jwts.builder() // JWT 토큰 빌더 생성
            .setSubject(authentication.getName()) // 토큰의 subject를 인증 정보의 이름으로 설정
            .claim(AUTHORITIES_KEY, authorities) // 클레임에 권한 정보 설정
            .setExpiration(accessTokenExpiresIn) // 토큰 만료 시간 설정
            .signWith(key, SignatureAlgorithm.HS256) // 키와 알고리즘 사용해서 토큰 서명 생성
            .compact(); // 토큰을 문자열 형태로 변환

    return TokenDto.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .build();
    // 토큰 유형과 엑세스 토큰을 포함하는 TokenDto 객체에 생성된 토큰 정보를 담아 반환
  }

  public Authentication getAuthentication(String accessToken) {
    // 주어진 엑세스 토큰을 사용하여 추출한 클레임을 기반으로 인증 객체 생성하여 반환

    Claims claims = parseClaims(accessToken); // 주어진 엑세스 토큰에서 클레임 파싱하여 추출

    if (claims.get(AUTHORITIES_KEY) == null) { // 권한 정보가 없는 경우 예외 발생
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    // 클레임에서 추출한 권한 정보를 쉼표로 구분된 문자열에서 배열로 변환한 뒤, SimpleGrantedAuthority 객체로 매핑하여 권한 컬렉션 생성

    UserDetails principal = new User(claims.getSubject(), "", authorities);
    // 클레임에서 추출한 subject와 권한 정보를 사용하여 UserDetails 객체 생성
    // 비밀번호 "", 권한 정보를 포함한 authorities 컬렉션 전달

    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    // 인증 객체 생성하여 반환
  }

  public boolean validateToken(String token) {
    // 주어진 토큰의 유효성 검증
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      /*
      JWT를 파싱하기 위한 파서 빌더 생성
      파서에 서명 키 설정(토큰 유효성 검증)
      파서 빌더로부터 파서 생성
      생성된 파서를 사용하여 토큰의 서명 검증 및 클레임 파싱
       */
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
    // 토큰의 클레임 파싱하는 메서드
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
      // getBody() 호출하여 파싱된 JWT의 claims 반환
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}