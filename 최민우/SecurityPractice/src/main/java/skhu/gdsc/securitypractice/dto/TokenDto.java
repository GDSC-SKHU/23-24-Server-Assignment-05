package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto { // 토큰
    private String grantType; // 인증을 요청할 때 사용되는 문자열
    private String accessToken; // 엑세스 토큰 값으로, 보안을 위한 인증 토큰
}