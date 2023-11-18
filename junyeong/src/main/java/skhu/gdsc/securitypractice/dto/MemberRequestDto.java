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
public class MemberRequestDto { // 회원 정보를 담당하는 Member 클래스

  private String email; // 회원 이메일
  private String password; // 회원 비밀번호

  public Member toMember(PasswordEncoder passwordEncoder) { // Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
    return Member.builder() // Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
            .email(email) //  Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
            .password(passwordEncoder.encode(password)) //  Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
            .authority(Authority.ROLE_USER) //  Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
            .build(); //  Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
  }

  public Member toAdmin(PasswordEncoder passwordEncoder) { // Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
    return Member.builder() // Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
            .email(email) //  Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
            .password(passwordEncoder.encode(password)) //  Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
            .authority(Authority.ROLE_ADMIN) // Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
            .build(); // Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
  }

  public UsernamePasswordAuthenticationToken toAuthentication() { // 회원 인증을 위한 UsernamePasswordAuthenticationToken 객체를 생성
    return new UsernamePasswordAuthenticationToken(email, password); // 회원 인증을 위한 UsernamePasswordAuthenticationToken 객체를 생성
  }
}