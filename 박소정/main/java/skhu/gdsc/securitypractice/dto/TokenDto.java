package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder // 자동, 지원해주는 builder 패턴을 사용. MemberResponseDto를 같이 참고(수동)
public class TokenDto {
  private String grantType; // 인증 및 인가(권한) 위임 방법
  private String accessToken;
  /* accessToken
  * 인증 및 인가 서비스 구현을 위해 필요
  *
  * 로그인 시 백엔드 서버에서 토큰 만들어 보냄
  * 이후, 클라이언트에서 보내는 요청의 Authorization(인가) 헤더에 토큰을 담아 보낼 시
  * -> 서버에서 토큰을 디코딩하여 로그인한 회원을 확인하는 방식
  *
  * access token은 일반적으로 서버에서 따로 로그아웃 불가능 -> 따라서 보안 상의 이유로 만료 기간 짧게 설정
  *
  * 참고 자료: https://velog.io/@ohzzi/Access-Token%EA%B3%BC-Refresh-Token%EC%9D%84-%EC%96%B4%EB%94%94%EC%97%90-%EC%A0%80%EC%9E%A5%ED%95%B4%EC%95%BC-%ED%95%A0%EA%B9%8C
  *  */
}