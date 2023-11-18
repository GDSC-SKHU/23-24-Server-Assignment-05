package com.example.security.domain;

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
    // 데이터베이스에서 사용되는 멤버 ID 필드를 매핑
    private Long id;

    private String email;
    // 멤버의 이메일을 나타내는 필드

    private String password;
    // 멤버의 비밀번호를 나타내는 필드

    @Enumerated(EnumType.STRING) //열거형 타입을 매핑하는 어노테이션
    // EnumType.STRING을 사용하여 Enum의 문자열 값을 데이터베이스에 저장
    private Authority authority;
    // 멤버의 권한을 나타내는 필드

    @Builder
    public Member(String email, String password, Authority authority) {
        this.email = email;
        this.password = password;
        this.authority = authority;
    }
}