package com.example.security.dto;

import com.example.security.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {

    private String email;
    // 멤버의 이메일을 나타내는 필드

    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getEmail());
        // 주어진 Member 객체로 MemberResponseDto를 생성하여 반환
    }
}