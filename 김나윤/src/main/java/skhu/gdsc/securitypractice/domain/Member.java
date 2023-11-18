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
  // AUTO_INCREMENT
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  // PK
  private Long id;

  private String email;

  private String password;

  // 유저 권한 정보 (EnumType.STRING: Enum 이름을 컬럼에 저장 / .ORDINAL: Enum의 순서값을 컬럼에 저장)
  @Enumerated(EnumType.STRING)
  private Authority authority;

  @Builder
  public Member(String email, String password, Authority authority) {
    this.email = email;
    this.password = password;
    this.authority = authority;
  }
}
