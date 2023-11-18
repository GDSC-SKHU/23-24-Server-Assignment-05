package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

  // OAuth2.0 프로토콜에서 사용하는 JWT 인증 타입
  private String grantType;
  // 인증 성공시 accessToken 발급
  private String accessToken;
}