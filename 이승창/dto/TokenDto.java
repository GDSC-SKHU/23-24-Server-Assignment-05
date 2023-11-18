package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter    // 게터 어노테이션: 클래스 속성을 대상으로 get-- 메소드 작성해줌.
@NoArgsConstructor
@AllArgsConstructor
@Builder // 빌더 어노테이션: 객체 생성해줌. @Setter보다 @Builder 사용 권장.
public class TokenDto {
  private String grantType;
  private String accessToken;
}