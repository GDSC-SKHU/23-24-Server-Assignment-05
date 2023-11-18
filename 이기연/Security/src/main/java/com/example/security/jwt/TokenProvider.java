package com.example.security.jwt;

import com.example.security.dto.TokenDto;
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
// 롬복을 사용하여 로깅을 위한 Logger를 자동으로 생성
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    // 토큰에서 권한 정보를 가져오기 위한 키
    private static final String BEARER_TYPE = "Bearer";
    // Bearer 토큰의 타입을 나타내는 상수
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    // 액세스 토큰의 만료 시간

    private final Key key;
    // 토큰 서명에 사용되는 키

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        // 생성자에서 secretKey 값을 받아 토큰 서명에 사용될 키를 초기화
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(Authentication authentication) {
        // 인증 객체를 사용하여 토큰을 생성하는 메서드

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        // 권한 정보를 쉼표로 구분된 문자열로 변환

        long now = (new Date()).getTime();
        // 현재 시간을 가져옴

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        // 액세스 토큰의 만료 시간을 계산

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                // 토큰의 주제를 설정 (사용자 이름)
                .claim(AUTHORITIES_KEY, authorities)
                // 토큰에 권한 정보를 설정
                .setExpiration(accessTokenExpiresIn)
                // 토큰의 만료 시간을 설정
                .signWith(key, SignatureAlgorithm.HS256)
                // 토큰을 서명함
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                // 토큰의 부여 타입을 설정
                .accessToken(accessToken)
                // 생성된 액세스 토큰을 설정
                .build();
        // TokenDto 객체를 빌더 패턴을 사용하여 생성
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰에서 Authentication 객체를 추출하는 메서드

        Claims claims = parseClaims(accessToken);
        // 토큰에서 클레임(클레임 세트)을 추출

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        // 토큰에서 추출한 권한 정보를 GrantedAuthority 컬렉션으로 변환

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        // 토큰의 주제를 사용하여 UserDetails를 생성

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
        // 생성된 UserDetails와 권한 정보로 UsernamePasswordAuthenticationToken을 반환
    }

    public boolean validateToken(String token) {
        // 토큰의 유효성을 검증하는 메서드
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            // 토큰을 파싱하여 검증
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
        // 토큰에서 클레임(클레임 세트)을 추출하는 메서드
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
            // 토큰을 파싱하여 클레임을 추출
        } catch (ExpiredJwtException e) {
            return e.getClaims();
            // 토큰이 만료된 경우, 만료된 클레임을 반환
        }
    }
}
