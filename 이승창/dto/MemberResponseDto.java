package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skhu.gdsc.securitypractice.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {

  private String email;
  public static MemberResponseDto of(Member member) { // builder로 변경 가능
    return new MemberResponseDto(member.getEmail());  // 멤버 객체를 받아 멤버디티오 객체로 변환
  }
}