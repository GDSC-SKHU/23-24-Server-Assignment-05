package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skhu.gdsc.securitypractice.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    // Member 객체를 받아와서 해당 멤버의 이메일 값을 사용하여 MemberResponseDto 객체를 생성
    private String email;
    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getEmail());
    }
}