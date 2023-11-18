package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skhu.gdsc.securitypractice.domain.Member;

@Getter
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 생성
@NoArgsConstructor // 파라미터가 없는 생성자를 생성
public class MemberResponseDto { // 회원 정보를 담당하는 Member 클래스

  private String email; // 회원 이메일
  public static MemberResponseDto of(Member member) {
    return new MemberResponseDto(member.getEmail());
  } // Member 객체를 MemberResponseDto 객체로 변환
}