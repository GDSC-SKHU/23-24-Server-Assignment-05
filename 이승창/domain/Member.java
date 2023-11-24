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
@Getter // 덕분에 member.getEmail로 접근 가능
@NoArgsConstructor   // 기본 생성자 만들어 줌.
public class Member {

  @Id   // PK선언
  @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임한다.
  @Column(name = "member_id")
  private Long id;

  private String email;

  private String password;

  @Enumerated(EnumType.STRING) // EnumType.STRING = 각 Enum 이름을 컬럼에 저장한다. ex)ROLE_USER, ROLE_ADMIN
  private Authority authority;  // EnumType.ORDINAL = 각 Enum에 대응되는 순서를 칼럼에 저장.ex) 0, 1
  // 이름으로 저장하느냐 또는 요소 번호로 컬럼에 저장하느냐
  @Builder  // 객체 생성
  public Member(String email, String password, Authority authority) {
    this.email = email;
    this.password = password;
    this.authority = authority;
  }
}
