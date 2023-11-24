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

  @Enumerated(EnumType.STRING) // 열거형 타입을 문자열로 저장
  private Authority authority; // 권한을 나타내는 필드

  @Builder
  public Member(String email, String password, Authority authority) {
    this.email = email;
    this.password = password;
    this.authority = authority;
  }
}
