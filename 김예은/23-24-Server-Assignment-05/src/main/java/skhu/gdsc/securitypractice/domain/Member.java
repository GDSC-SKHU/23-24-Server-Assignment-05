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
  @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임 , 즉  id 값을 null로 하면 DB가 알아서 AUTO_INCREMENT 해줌
  @Column(name = "member_id") // 연관관계 매핑
  private Long id;
  private String email; // private 맴버 변수 email
  private String password; // private 맴버 변수 password

  @Enumerated(EnumType.STRING)
  /*
   * 해당 enum의 name 즉, 값이 그대로 들어가지게 된다.
   * 즉, DB에 enum name 값을 넣을 때는 위의 어노테이션을 이용해서 enum의 값이
   * 텍스트 그대로 들어가지게 entity의 column에 어노테이션 설정을 해줘야 한다.
   * */
  private Authority authority;

  @Builder
  public Member(String email, String password, Authority authority) { // 생성자
    this.email = email;
    this.password = password;
    this.authority = authority;
  }
}
