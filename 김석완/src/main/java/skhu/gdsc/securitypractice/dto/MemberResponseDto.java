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
  public static MemberResponseDto of(Member member) { // ResponseDto이므로 Member를 MemberResponseDto로 변환
    return new MemberResponseDto(member.getEmail());
  }
}