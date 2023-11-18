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
public class Member { // 회원 정보를 담당하는 Member 클래스

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동으로 생성되는 값을 사용
  @Column(name = "member_id") // DB에서 member_id 컬럼으로 매핑
  private Long id; // 회원 고유 번호

  private String email; // 회원 이메일

  private String password; // 회원 비밀번호

  @Enumerated(EnumType.STRING) // Enum 타입을 DB에 String 타입으로 저장
  private Authority authority; // 회원 권한

  @Builder
  public Member(String email, String password, Authority authority) { // Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
    this.email = email; // Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
    this.password = password; // Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
    this.authority = authority; // Member 객체를 생성할 때, 필요한 값들을 매개변수로 받음
  }
}
