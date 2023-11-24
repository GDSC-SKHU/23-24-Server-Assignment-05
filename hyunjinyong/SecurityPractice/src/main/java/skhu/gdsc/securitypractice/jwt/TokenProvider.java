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

//token을 만들어 제공해주는 클래스다.
@Slf4j
@Component
public class TokenProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final String BEARER_TYPE = "Bearer";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분, 만료시간을 의미.

  private final Key key;

  /*사용하고자하는 secret key를 decoding하여 byte배열로 전환해준 뒤, key로 만들어주는 것이다.
  * 여기서 hmacShaKeyFor(), 이 메소드가 뭔가 싶었는데
  * "HMAC-SHA 알고리즘을 통해 secret key에 대해 암호화를 시켜주는 메소드"라고 한다.
  * 즉 이 생성자는
  * jwt.secret에 저장한 것을 가져옴 -> base64 자료형으로 decoding -> hmacShaKeyFor()로 다시 암호화 및 key로 지정*/
  public TokenProvider(@Value("${jwt.secret}") String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  //토큰생성 메소드이다.
  public TokenDto generateTokenDto(Authentication authentication) {
    //authenticaton 객체에서 권한을 가져와서 String 타입 변수에 저장하는 코드다.
    /*GrantedAuthority타입 객체에서 권한(authority)만 String 형식으로 추출한다.
    * collect로 필터링된 요소들을 쉼표로 구분하여 저장한다.*/
    String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    long now = (new Date()).getTime();

    //accessToken의 만료시한을 정해놓고 발행한다.
    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
    String accessToken = Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    //Dto로 변환하여 반환한다.
    return TokenDto.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .build();
  }

  //accessToken을 파라미터로 받아 Authentication 객체를 생성하는 메소드다.
  public Authentication getAuthentication(String accessToken) {
    //claims는 페이로드 부분에 들어가는 정보의 한 덩어리다. accessToken에서 claims를 파싱한다.
    Claims claims = parseClaims(accessToken);

    //파싱한 claims에서 권한 부분이 추출했는데 null일 경우, 권한 정보가 없다고 판단한다.
    //비유하자면 영화티켓을 직원에게 보여줬는데 상영중인 영화티켓이 아니기에 볼 자격이 없다는 말을 들은 것과 같다.
    if (claims.get(AUTHORITIES_KEY) == null) {
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    /*변수 claims에서 권한 정보(AUTHORITIES_KEY)를 가져와서 쉼표로 구분하여 문자열로 반환한다.
    * SimpleGrantedAuthority 객체로 반환한 후에 collection 타입의 authorities에 저장한다.*/
    Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    //claims에서 subject(토큰 제목)을 가져와서 user 객체 생성
    //username과 password, authorities를 갖고 있다.
    UserDetails principal = new User(claims.getSubject(), "", authorities);
    //객체 생성후 반환
    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  //token을 검증하는 메소드이다.
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
  //accessToken에서 claims 부분을 파싱하는 메소드
  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}