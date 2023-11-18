package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto { //토큰에 사용될 Dto
  private String grantType; //권한 부여 코드승인 타입
  private String accessToken; //접근하는데 사용될 토큰
}