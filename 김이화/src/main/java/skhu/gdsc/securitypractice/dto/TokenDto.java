package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto { // 토큰 정보 전달
  private String grantType; // 토큰 부여 유형
  private String accessToken; // 엑세스 된 토큰 값(접근 제한)
}