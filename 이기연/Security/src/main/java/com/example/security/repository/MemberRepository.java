package com.example.security.repository;

import java.util.Optional;

import com.example.security.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    // 이메일을 기반으로 멤버를 조회하는 메서드입니다. Optional을 사용하여 결과가 없을 때 null을 방지

    boolean existsByEmail(String email);
    // 주어진 이메일을 가진 멤버가 존재하는지 확인하는 메서드입니다. 존재하면 true, 아니면 false를 반환
}
