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

  // 유저 email 반환
  public static MemberResponseDto of(Member member) {
    return new MemberResponseDto(member.getEmail());
  }
}