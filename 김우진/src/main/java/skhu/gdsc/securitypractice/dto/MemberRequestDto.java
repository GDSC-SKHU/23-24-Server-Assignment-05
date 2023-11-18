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
public class MemberRequestDto { //사용자 요청 Dto

  private String email;
  private String password;

  public Member toMember(PasswordEncoder passwordEncoder) { //PasswordEncoder을 통해서 토큰을 받아서 검증 후 사용자 정보를 빌드함
    return Member.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .authority(Authority.ROLE_USER)
            .build();
  }

  public Member toAdmin(PasswordEncoder passwordEncoder) { //마찬가지로 PasswordEncoder를 통해서 검증 후 관리 정보를 빌드함
    return Member.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .authority(Authority.ROLE_ADMIN)
            .build();
  }

  //사용자 인증 정보를 확인하는데 사용함, 인증이 성공하면 이 정보가 보안 컨텍스트안에 저장됨
  public UsernamePasswordAuthenticationToken toAuthentication() {
    return new UsernamePasswordAuthenticationToken(email, password);
  }
}