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
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 토큰 만료 시간 30분으로 설정

  private final Key key;

  //jwt.secret property값을 받아와서 실행 할 수 있음을 재정의
  public TokenProvider(@Value("${jwt.secret}") String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey); //secretKey를 base64디코딩을 해서 keybytes배열에 저장
    this.key = Keys.hmacShaKeyFor(keyBytes); // keyBytes를 사용해서 HMAC-SHA키를 생성해서 this.key에 저장
  }

  public TokenDto generateTokenDto(Authentication authentication) { // TokenDto를 반환하는 메서드

    String authorities = authentication.getAuthorities().stream() //stream을 통해서 하나씩 처리
            .map(GrantedAuthority::getAuthority) // 권한 객체를 권한 이름으로 각각 매핑함
            .collect(Collectors.joining(",")); // 권한 이름을 쉼표로 구분한 문자열로 반환

    long now = (new Date()).getTime(); //now에 현재 시간 정보를 저장

    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME); // 현재시간 + 30분을 더해서 만료시간을 accessTokenExprieIn에 저장
    String accessToken = Jwts.builder() // jwt 토큰 생성
            .setSubject(authentication.getName()) // authentication객체에서 이름을 반환
            .claim(AUTHORITIES_KEY, authorities) // 권한 정보를 나타내는 Authorities_key를 통해서 토큰에 추가적으로 클레임 권한 정보를 담은 authorities를 저장
            .setExpiration(accessTokenExpiresIn) // //토큰 만료시간 설정
            .signWith(key, SignatureAlgorithm.HS256) // 토큰에 사용할 서명 알고리즘중에서 HS256을 사용함
            .compact(); //jwt토큰을 문자열로 바꿔서 반환함

    return TokenDto.builder()
            .grantType(BEARER_TYPE) // 토큰의 발급 유형 나타냄
            .accessToken(accessToken) // 완성된 접근 토큰을 설정함
            .build(); // TokenDto객체 생성후 반환
  }

  //주어진 accessToken을 파싱해서 권한 정보를 추출하고 그 정보를 토대로 인증객체를 반환하는 메서드
  public Authentication getAuthentication(String accessToken) {

    Claims claims = parseClaims(accessToken); // claims에 JWT 토큰에 저장된 정보를 저장함

    if (claims.get(AUTHORITIES_KEY) == null) { //key값이 널이면 예외 터트림
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    //claims에서 권한 정보를 추출해서 authorities에 저장함
    Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))//claims에서 권한 정보를 쉽표로 구분해서 문자열 배열로 저장
                    .map(SimpleGrantedAuthority::new) //권한 이름을 simplegrantedauthority를 통해서 객체로 다시 변환
                    .collect(Collectors.toList()); //컬렉션 형태로 변환

    //토큰의 subject정보를 가져와서 사용자 이름으로 설정하고, 빈 비밀번호, 권한정보를 사용해서 principal객체를 생성함
    UserDetails principal = new User(claims.getSubject(), "", authorities);

    // UsernamePasswordAuthenticationToken객체를 생성해서 인증 객체로 반환
    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  public boolean validateToken(String token) { //유효성 검증을 위한 boolean타입 메서드
    try {
      // 주어진 토큰을 파싱하여 검증함
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;

      //유효하지 않은 토큰일 경우 false를 반환하고 로그를 출력함
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

  private Claims parseClaims(String accessToken) { //토큰을 파싱해서 Claims 정보를 반환함
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken) // 클레임 정보를 얻어옴
              .getBody(); // 클레임 정보를 반환함
    } catch (ExpiredJwtException e) { //파싱중에 토큰 시간이 만료가 되었으면 예외를 터트려 ExpiredJwtException을 통해서 해당 클레임 정보를 반환함
      return e.getClaims();
    }
  }
}