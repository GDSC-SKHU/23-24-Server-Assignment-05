package skhu.gdsc.securitypractice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import skhu.gdsc.securitypractice.domain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
  Optional<Member> findByEmail(String email); //이메일을 찾는~
  boolean existsByEmail(String email); //이메일이 존재하는지 여부를~
}
