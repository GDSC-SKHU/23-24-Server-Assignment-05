package Security.example.demo.jwt;


import Security.example.demo.dto.TokenDto;
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

@Slf4j
@Component
public class TokenProvider {  // JWT를 검사하고 유효성을 검사

    private static final String AUTHORITIES_KEY = "auth";  // 토큰의 권한 정보 저장하는 키
    private static final String BEARER_TYPE = "Bearer";  // 토큰의 타입을 나내는 문자열
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 엑세스 토큰의 만료 시간

    private final Key key;

    // BASE-64 인코딩된 바이트배열을 디코딩하여 Key 객체로 변환
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    // Authentication 객체를 받아서 토큰을 생성하고 TokenDto타입 객체로 반환
    public TokenDto generateTokenDto(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));  // 권한 정보 문자열 authorities에 저장

        long now = (new Date()).getTime(); // 현재 시간 저장

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);  // 현재 시간을 기준으로 토큰 만료 시간 계산
        String accessToken = Jwts.builder()  // Jwts 빌더 객체 생성
                .setSubject(authentication.getName())  // 사용자의 이름을 토큰의 subject로 설정
                .claim(AUTHORITIES_KEY, authorities)  // 클레임 생
                .setExpiration(accessTokenExpiresIn)  // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)  // 서성 생성
                .compact();  // JWT 문자열 생성

        return TokenDto.builder()// TokenDto 객체 반환
                .grantType(BEARER_TYPE)  // 토큰 타입
                .accessToken(accessToken)  // JWT 문자열
                .build();
    }

    // 엑세스 토큰을 받아서 Authentication 객체 반환
    public Authentication getAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);  // 클레임 저장

        if (claims.get(AUTHORITIES_KEY) == null) {  // 권한없으면 예외처리
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))  // 클레임을 문자열로 변환 후 ,기준으로 분리
                        .map(SimpleGrantedAuthority::new)  // SimpleGrantedAuthority로 맵핑
                        .collect(Collectors.toList());  // 리스트로 변환

        UserDetails principal = new User(claims.getSubject(), "", authorities);  // 유저 객체 생성(사용자의 식별 정보 전달, 비밀번호는 없음, 사용자의 권한 설정 )

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);  // 사용자의 정보와 권한을 담고 있는 UsernamePasswordAuthenticationToken 객체 반환
    }

    public boolean validateToken(String token) { // 잘못된 토큰 예외처리
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);  // 서명 정보 가지고 있는 토큰
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

    // 엑세스 토큰을 받아서 JWT객체로 반환, 만료된 토큰일 경우 예외 발생
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();  // 토큰의 서명 확인
        } catch (ExpiredJwtException e) {  // 만료됐을 시 Claim 반환
            return e.getClaims();
        }
    }
}