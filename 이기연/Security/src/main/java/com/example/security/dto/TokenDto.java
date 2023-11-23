package com.example.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String grantType;
    // 토큰의 부여 타입을 나타내는 필드
    private String accessToken;
    // 액세스 토큰을 나타내는 필드
}
