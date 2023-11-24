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
// 데이터베이스 테이블과 매핑
@Getter
@NoArgsConstructor
//Lombok. 파라미터가 없는 디폴트 생성자를 생성하여 명시적으로 선언된 생성자가 없더라도 인스턴스 생성
public class Member { //회원 정보

  @Id
  // pk
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  // 자동 생성
  @Column(name = "member_id")
  //데이터베이스 테이블의 컬럼과 매핑, 이름 지정
  private Long id; // 고유값

  private String email; // 회원 이메일

  private String password; // 회원 비밀번호

  @Enumerated(EnumType.STRING)
  // Authority 열거형과 매핑(해당 변수를 문자열 형태로 저장하고 읽어옴)
  private Authority authority; // 회원 권한 정보

  @Builder
  // 빌더 패턴을 사용하여 객체 생성
  public Member(String email, String password, Authority authority) {
    this.email = email;
    this.password = password;
    this.authority = authority;
  }
}
