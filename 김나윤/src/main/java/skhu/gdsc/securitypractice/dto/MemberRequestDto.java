package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import skhu.gdsc.securitypractice.domain.Authority;
import skhu.gdsc.securitypractice.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

  private String email;
  private String password;

  // 유저 객체로 변환
  public Member toMember(PasswordEncoder passwordEncoder) {
    return Member.builder()
            .email(email)
            // 비밀번호 암호화
            .password(passwordEncoder.encode(password))
            // 일반 유저 권한
            .authority(Authority.ROLE_USER)
            .build();
  }

  // 관리자 객체로 변환
  public Member toAdmin(PasswordEncoder passwordEncoder) {
    return Member.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            // 관리자 권한
            .authority(Authority.ROLE_ADMIN)
            .build();
  }

  // 토큰으로 변환
  public UsernamePasswordAuthenticationToken toAuthentication() {
    return new UsernamePasswordAuthenticationToken(email, password);
  }
}