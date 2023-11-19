package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skhu.gdsc.securitypractice.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto { // 회원 정보 전달

  private String email; // 이메일을 통해 회원 정보 표현
  public static MemberResponseDto of(Member member) {
    // Member 객체를 통해 회원 정보 전달하는 메서드
    return new MemberResponseDto(member.getEmail()); // Member 객체의 이메일을 얻어 회원 정보 전달
  }
}