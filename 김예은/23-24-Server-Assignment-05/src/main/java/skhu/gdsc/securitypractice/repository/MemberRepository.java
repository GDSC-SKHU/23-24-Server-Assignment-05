package skhu.gdsc.securitypractice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import skhu.gdsc.securitypractice.domain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
  Optional<Member> findByEmail(String email);
  boolean existsByEmail(String email);

  // Email 을 Login ID 로 갖고 있기 때문에 findByEmail 와 중복 가입 방지를 위한 existsByEmail 만 추가
}
