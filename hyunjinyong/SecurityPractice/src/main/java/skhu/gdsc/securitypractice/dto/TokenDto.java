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
  //jwt가 될 dto 클래스다.
  //grant type은 인증 승인 방식을 의미한다.
  private String grantType;
  //액세스토큰은 인증에 성공했을 때 발급받는 토큰이다.
  private String accessToken;
}