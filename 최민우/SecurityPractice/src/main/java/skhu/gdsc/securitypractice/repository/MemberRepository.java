package skhu.gdsc.securitypractice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import skhu.gdsc.securitypractice.domain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> { // JpaRepository를 확장한 인터페이스
    Optional<Member> findByEmail(String email); // 해당 이메일을 갖는 Member를 찾는다.
    boolean existsByEmail(String email); // 주어진 이메일을 가진 회원이 데이터베이스에 존재하는지 여부를 확인
}