package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skhu.gdsc.securitypractice.domain.Member;

/* Dto : 데이터 전송 객체로 주로 데이터를 저장하거나 전송하는데 쓰이는 객체
 * 데이터만 가지고 있는 객체 , 도메인을 캡슐화하여 보호하기 좋음
 * MemberResponseDto는 회원가입 응답 Dto
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {

  private String email;
  public static MemberResponseDto of(Member member) {
    return new MemberResponseDto(member.getEmail());
  }
}