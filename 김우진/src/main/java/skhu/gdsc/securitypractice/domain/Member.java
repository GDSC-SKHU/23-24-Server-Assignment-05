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
  @GeneratedValue(strategy = GenerationType.IDENTITY)// identity로 pk를 기본 생성후 자동으로 값이 하나씩 증가함
  @Column(name = "MEMBER_ID")
  private Long id;

  private String email;

  private String password;

  @Enumerated(EnumType.STRING) //권한을 문자열로 저장
  private Authority authority;

  @Builder
  public Member(String email, String password, Authority authority) { //주입
    this.email = email;
    this.password = password;
    this.authority = authority;
  }
}
