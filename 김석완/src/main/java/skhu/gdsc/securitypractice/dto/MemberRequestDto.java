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

  public Member toMember(PasswordEncoder passwordEncoder) {
    return Member.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .authority(Authority.ROLE_USER)
            .build();
  }

  public Member toAdmin(PasswordEncoder passwordEncoder) {
    return Member.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .authority(Authority.ROLE_ADMIN)
            .build();
  }

  public UsernamePasswordAuthenticationToken toAuthentication() { // email과 password를 포함한 토큰을 생성하는 메서드
    return new UsernamePasswordAuthenticationToken(email, password);
  }
}