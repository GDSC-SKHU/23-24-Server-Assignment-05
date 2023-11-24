package skhu.gdsc.securitypractice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor // 파라미터 없는 기본 생성자 생성(헷갈려서 계속 적을 예정)
public class Member {

  @Id // 기본키 할당
  @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키를 자동 생성하기 위해 사용.
  @Column(name = "member_id")
  private Long id;
  /*
  * @GeneratedValue(strategy = GenerationType.IDENTITY)
  * 기본키 생성을 데이터베이스에게 위임하는 방식
  * id 값을 따로 할당X, 데이터베이스가 자동으로 AUTO_INCREMENT 하여 생성
  *
  * JPA는 보통 영속성 컨텍스트에서 객체를 관리하다가 commit이 호출되는 시점에 쿼리문을 실행
  * 하지만 IDENTITY 전략에서는 EntityManager.persist()를 하는 시점에 Insert SQL을 (추가)실행하여 데이터베이스에서 식별자를 조회
  *
  * 왜냐하면,
  * 영속성 컨텍스트는 1차 캐시에 PK와 객체를 가지고 관리
  * 기본키를 데이터베이스에게 위임햇기 때문에 EntityManager.persist()호출 하더라도 데이터베이스에 값을 넣기 전까지 기본키를 모름 -> 관리 불가능
  * 그래서, IDENTITY 전략에서는 EntityManager.persist()를 하는 시점에 Insert SQL을 (추가)실행
  * -> 데이터베이스에서 식별자를 조회하여 영속성 컨텍스트 1차 캐시에 값을 넣어줌 -> 관리 가능
  *
  * 참고 자료: https://velog.io/@gudnr1451/GeneratedValue-%EC%A0%95%EB%A6%AC
  * */

  private String email;

  private String password;

  @Enumerated(EnumType.STRING) // enum의 값을 index가 아닌 텍스트 값 그대로 저장. 기본 값은 @Enumerated(value = EnumType.ORDINAL) 또는 @Enumerated(EnumType.ORDINAL)로 (+아무 설정을 하지 않을 경우) 해당 enum값의 index가 DB에 들어감.
  private Authority authority;

  @Builder
  public Member(String email, String password, Authority authority) {
    this.email = email;
    this.password = password;
    this.authority = authority;
  }
}

/*
* 도메인(domain)
* 해결하고자 하는 문제의 영역
* 요구사항, 용어, 기능
*
* 도메인 모델
* 1) Entity
* 실제, 객체
* 정보를 저장 및 관리하기 위한 집합적인 것
* 식별자 가짐 -> 식별자에 들어가지 않는 데이터가 변경되어도 같은 객체
*
* 2) Value
* 말 그대로 값 그 자체
* 식별자 X -> 하나의 데이터라도 변경된다면 다른 객체
*
* 참고 자료: https://runa-nam.tistory.com/120
* */
