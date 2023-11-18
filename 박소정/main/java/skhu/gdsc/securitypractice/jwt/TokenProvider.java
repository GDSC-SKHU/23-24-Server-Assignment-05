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

@Slf4j // 롬복 라이브러리를 사용하여 로깅을 위한 코드를 자동으로 생성. 로깅: 프로그램이 실행될 때 중요한 정보나 이벤트를 기록하는 것을 의미. 기록된 정보는 이벤트 추적 및 디버깅에 사용
@Component // 해당 어노테이션이 붙은 클래스는 스프링에서 관리하는 컴포넌트(빈)로 인식되며, 스프링 애플리케이션 컨텍스트로 등록. 사용 목적: 1. 빈 등록, 2. 의존성 주입
public class TokenProvider {
// 토큰의 생성, 토큰의 유효성 검증을 담당

  private static final String AUTHORITIES_KEY = "auth"; // JWT 페이로드에서 권한 정보를 나타내는 키, 사용자에 대한 정보를 JSON 형태로 포함
  private static final String BEARER_TYPE = "Bearer"; // HTTP Authorization 헤더에서 사용되는 Bearer 토큰의 타입을 나타내는 값
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분, accessToken의 만료 시간을 밀리초 단위로 환산. JWT 토큰의 만료 시간을 정의하는 상수

  private final Key key;

  public TokenProvider(@Value("${jwt.secret}") String secretKey) { // 프로퍼티 값 주입
    byte[] keyBytes = Decoders.BASE64.decode(secretKey); // 주입받은 secretKey를 BASE64 디코딩하여 바이트 배열로 변환
    this.key = Keys.hmacShaKeyFor(keyBytes); // Keys.hmacShaKeyFor(keyBytes)를 사용하여 HMAC-SHA 키를 생성, 생성된 key 객체는 HMAC-SHA256 알고리즘에 사용될 비밀 키
  } // jwt.secret 프로퍼티 값을 받아와서, 이 값을 사용하여 JWT 토큰 서명에 사용될 비밀 키를 생성

  public TokenDto generateTokenDto(Authentication authentication) { // Authentication(인증) 객체를 기반으로 JWT(Json Web Token)를 생성하고, 이를 포함한 TokenDto를 반환하는 역할

    String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(",")); // 주어진 Authentication 객체에서 사용자의 권한 정보를 추출

    long now = (new Date()).getTime(); // 토큰의 발행 시간, 현재 시간을 밀리초 단위로 저장

    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME); // accessToken의 만료 시간 설정
    String accessToken = Jwts.builder() // JWT 토큰을 생성
            .setSubject(authentication.getName()) // 사용자명 설정
            .claim(AUTHORITIES_KEY, authorities) // 사용자의 권한 정보
            .setExpiration(accessTokenExpiresIn) // 토큰의 만료 시간 설정
            .signWith(key, SignatureAlgorithm.HS256) // HS256 알고리즘을 적용하여 서명(시그니쳐)를 생성
            .compact(); // JWT 문자열로 변환

    return TokenDto.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken) // 생성된 accessToken을 포함
            .build(); // 생성된 JWT 토큰과 함께 TokenDto 객체를 생성하여 반환
  }

  public Authentication getAuthentication(String accessToken) { // 주어진 액세스 토큰을 기반으로 사용자의 인증 정보(Authentication)를 생성하여 반환하는 역할

    Claims claims = parseClaims(accessToken);

    if (claims.get(AUTHORITIES_KEY) == null) {
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

    UserDetails principal = new User(claims.getSubject(), "", authorities); // 클레임에서 추출한 사용자명과 권한 정보를 사용하여 UserDetails 객체를 생성, 패스워드는 보안상의 이유로 토큰에 저장X

    return new UsernamePasswordAuthenticationToken(principal, "", authorities); // 스프링 시큐리티에서 인증을 나타내는 클래스로, 인증된 사용자의 정보를 가짐. principal과 authorities 정보를 사용하여 생성
  }

  public boolean validateToken(String token) { //  주어진 JWT의 유효성을 검증하는 역할
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // 수신한 토큰을 검증
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

  private Claims parseClaims(String accessToken) { // 액세스 토큰을 검증하고 클레임 정보를 추출하는 역할
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}