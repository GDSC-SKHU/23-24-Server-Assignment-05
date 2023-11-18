package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import skhu.gdsc.securitypractice.domain.Authority;
import skhu.gdsc.securitypractice.domain.Member;

/* Dto : 데이터 전송 객체로 주로 데이터를 저장하거나 전송하는데 쓰이는 객체
 * 데이터만 가지고 있는 객체 , 도메인을 캡슐화하여 보호하기 좋음
 * MemberRequestDto의 경우 회원가입, 로그인 요청에 사용할 dto
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

  private String email; // private 맴버 변수 email
  private String password; // private 맴버 변수 password

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

  public UsernamePasswordAuthenticationToken toAuthentication() {
    return new UsernamePasswordAuthenticationToken(email, password);
  }
}