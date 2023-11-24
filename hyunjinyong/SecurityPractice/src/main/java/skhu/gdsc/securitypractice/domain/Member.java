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
@NoArgsConstructor
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  private String email;

  private String password;

  //enum type을 엔티티 속성으로 쓰기 위한 어노테이션이다. 뒤에 (EnumType.String)은
  //enum의 이름을 db에 저장한다는 의미다. 참고로 (EnumType.ORDINAL)은 enum의 순서를
  //db에 저장한다. 전자의 경우, role_user와 role_admin이 db에 저장된다고 보면 된다.
  @Enumerated(EnumType.STRING)
  private Authority authority;

  @Builder
  public Member(String email, String password, Authority authority) {
    this.email = email;
    this.password = password;
    this.authority = authority;
  }
}
