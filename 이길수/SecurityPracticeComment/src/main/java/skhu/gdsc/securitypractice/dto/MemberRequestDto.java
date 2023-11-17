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

  // Member 정보 요청 DTO
  public Member toMember(PasswordEncoder passwordEncoder) {
    return Member.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .authority(Authority.ROLE_USER)
            .build();
  }

  // Admin 정보 요청 DTO
  public Member toAdmin(PasswordEncoder passwordEncoder) {
    return Member.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .authority(Authority.ROLE_ADMIN)
            .build();
  }

  /*UsernamePasswordAuthenticationToken
  추후 인증이 끝나고 SecurityContextHolder.getContext()에 등록될 Authentication 객체이다.
   */
  public UsernamePasswordAuthenticationToken toAuthentication() {
    return new UsernamePasswordAuthenticationToken(email, password);
  }
}