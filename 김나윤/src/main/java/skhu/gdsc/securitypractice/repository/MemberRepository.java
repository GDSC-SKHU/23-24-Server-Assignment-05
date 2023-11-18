package skhu.gdsc.securitypractice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import skhu.gdsc.securitypractice.domain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
  // email로 member 조회
  Optional<Member> findByEmail(String email);
  // 주어진 email로 member 존재 여부 확인
  boolean existsByEmail(String email);
}
