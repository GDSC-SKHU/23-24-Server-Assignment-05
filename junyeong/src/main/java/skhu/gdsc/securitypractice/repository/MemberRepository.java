package skhu.gdsc.securitypractice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import skhu.gdsc.securitypractice.domain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> { // Member 객체를 관리하는 MemberRepository 인터페이스
  Optional<Member> findByEmail(String email); // 회원 이메일로 회원 정보를 조회
  boolean existsByEmail(String email); // 회원 이메일로 회원 정보가 존재하는지 확인
}
