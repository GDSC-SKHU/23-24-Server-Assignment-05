package com.example.security.dto;

import com.example.security.domain.Authority;
import com.example.security.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    private String email;
    // 멤버의 이메일을 나타내는 필드

    private String password;
    // 멤버의 비밀번호를 나타내는 필드

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                // 이메일 값을 설정
                .password(passwordEncoder.encode(password))
                // 비밀번호를 인코딩하여 설정
                .authority(Authority.ROLE_USER)
                // 권한을 ROLE_USER로 설정
                .build();
        // Member 객체를 빌더 패턴을 사용하여 생성
    }

    public Member toAdmin(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                // 이메일 값을 설정
                .password(passwordEncoder.encode(password))
                // 비밀번호를 인코딩하여 설정
                .authority(Authority.ROLE_ADMIN)
                // 권한을 ROLE_ADMIN으로 설정
                .build();
        // Member 객체를 빌더 패턴을 사용하여 생성
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
        // UsernamePasswordAuthenticationToken을 생성하여 인증을 위한 토큰으로 반환
    }
}
