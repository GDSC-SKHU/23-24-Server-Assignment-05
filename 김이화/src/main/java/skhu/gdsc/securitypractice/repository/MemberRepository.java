package skhu.gdsc.securitypractice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import skhu.gdsc.securitypractice.domain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
  // 회원 정보 처리를 위한 데이터베이스 조회 및 조작 기능 제공
  // 인터페이스를 통해 Spring Data JPA에서 제공하는 CRUD 상속
  Optional<Member> findByEmail(String email); // 주어진 이메일과 일치하는 회원 조회하여 optional 객체로 반환
  boolean existsByEmail(String email); // 주어진 이메일과 일치하는 회원이 존재한다면 true, 존재하지 않으면 false
}
