package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* Dto : 데이터 전송 객체로 주로 데이터를 저장하거나 전송하는데 쓰이는 객체
 * 데이터만 가지고 있는 객체 , 도메인을 캡슐화하여 보호하기 좋음
 * ToeknDto는 주로 로그인, 재발급 응답 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
  private String grantType;
  private String accessToken;
}