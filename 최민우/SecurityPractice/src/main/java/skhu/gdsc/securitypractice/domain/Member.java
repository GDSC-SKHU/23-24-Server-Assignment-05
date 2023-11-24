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
    private Long id; // id(기본키)

    private String email; // 이메일

    private String password; // 비밀번호

    @Enumerated(EnumType.STRING) // 엔티티 클래스에서 enum 타입을 데이터베이스에 저장하는 방식을 지정
    private Authority authority; // 해당 enum 상수의 순서 값(열거형 선언 내에서의 위치)이 아니라 상수의 이름을 문자열로 저장

    @Builder
    public Member(String email, String password, Authority authority) {
        this.email = email; // 이메일
        this.password = password; // 비밀번호
        this.authority = authority; // 권한
    }
}