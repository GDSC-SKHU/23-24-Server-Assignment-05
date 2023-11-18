package skhu.gdsc.securitypractice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import skhu.gdsc.securitypractice.domain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> { // JpaRepository 상속 받음 <Entity, id>
  Optional<Member> findByEmail(String email);
  boolean existsByEmail(String email);
}
