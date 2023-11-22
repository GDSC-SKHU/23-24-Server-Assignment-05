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
  public static MemberResponseDto of(Member member) { //이메일로 요청을 하는 메서드

    return new MemberResponseDto(member.getEmail());
  }
}