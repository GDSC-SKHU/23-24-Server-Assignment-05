package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto { // 토큰 정보를 담당하는 TokenDto 클래스
  private String grantType; // 토큰 타입
  private String accessToken; // 액세스 토큰
}