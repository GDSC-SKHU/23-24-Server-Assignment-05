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
//Lombok. 모든 필드 값을 파라미터로 받는 생성자 생성하여 한 번에 초기화 가능
@NoArgsConstructor
public class MemberRequestDto { // 회원 관련 요청 데이터 전달하여 회원 생성, 인증 구현

  private String email;
  private String password;
  // 회원 이메일과 비밀번호가 회원 생성과 인증에 사용

  public Member toMember(PasswordEncoder passwordEncoder) { // Member 객체로 변환
    return Member.builder() // 빌더 패턴을 사용하여 생성
            .email(email) // 입력받은 이메일
            .password(passwordEncoder.encode(password)) // 입력받은 비밀번호(passwordEncoder 사용하여 암호화)
            .authority(Authority.ROLE_USER) // 회원 권한 -> ROLE_USER로 설정
            .build();
  }

  public Member toAdmin(PasswordEncoder passwordEncoder) { // ROLE_ADMIN에 등록된 Member 객체로 변환
    return Member.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .authority(Authority.ROLE_ADMIN) // 회원 권한 -> ROLE_ADMIN으로 설정
            .build();
  }

  public UsernamePasswordAuthenticationToken toAuthentication() {
    //Spring Security에서 사용되는 UsernamePasswordAuthenticationToken 객체로 변환
    return new UsernamePasswordAuthenticationToken(email, password); // 이메일과 비밀번호를 인자로 받아 객체 인증에 사용
  }
}