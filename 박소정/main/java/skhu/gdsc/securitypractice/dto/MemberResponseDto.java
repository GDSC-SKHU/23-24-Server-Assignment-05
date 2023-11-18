package skhu.gdsc.securitypractice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skhu.gdsc.securitypractice.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {

  private String email;
  public static MemberResponseDto of(Member member) {
    return new MemberResponseDto(member.getEmail());
  }
  // of로 직접 선언하여 객체 생성(수동)
}

/*
* 데이터 전송 객체(DTO)
* 데이터 전송을 위해 생성되는 객체
* 중요한 정보를 노출시키지 않고(필요한 데이터만 전송) 두 시스템(API와 서버 등) 간 통신을 원활하게 촉진
*
* 참고 자료: https://www.okta.com/kr/identity-101/dto/
* */


/*
* 요청과 응답을 DTO로 변환하는(엔티티를 사용하면 안 되는) 이유
*
* 엔티티는 데이터베이스 영속성의 목적으로 사용되는 객체
* 요쳥, 응답을 전달하는 클래스로 함께 사용될 경우 변경될 가능성이 큼
* 데이터베이스에 직접적인 영향을 주기 때문에 DTO를 분리
*
* + 그 외에도 엔티티 객체 자체를 응답으로 보낼 때 순환참조 문제가 생김(이건 아직 이해 못 함)
* 참고 자료: https://jie0025.tistory.com/381
* */