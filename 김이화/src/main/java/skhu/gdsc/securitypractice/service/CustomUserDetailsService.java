package skhu.gdsc.securitypractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import skhu.gdsc.securitypractice.domain.Member;
import skhu.gdsc.securitypractice.repository.MemberRepository;

@Service
@RequiredArgsConstructor
//Lombok. final이나 @NotNull으로 선언된 필드만 파라미터로 받는 생성자를 생성하여 클래스가 의존하는 필드 초기화
public class CustomUserDetailsService implements UserDetailsService {
  // Spring Security의 UserDetailsService를 통해 사용자 인증 관련 기능 제공
  // 데이터베이스에서 회원 정보 조회, 사용자 정보 객체로 반환하여 사용자 정보를 확인하고 인증 수행

  private final MemberRepository memberRepository;

  @Override
  @Transactional
  // 오류가 발생했을 때 모든 작업들을 원상태로 되돌리고, 성공했을 때 최종적으로 데이터베이스에 반영
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // 주어진 username을 기반으로 데이터베이스에서 사용자를 조회하고 UserDetails 객체를 생성하여 반환
    // 사용자가 존재하지 않는다면 예외 발생
    return memberRepository.findByEmail(username)
            // 사용자명에 해당하는 회원 조회
            .map(this::createUserDetails)
            // Optional 객체의 map 메서드를 사용하여 조회된 회원 정보를 createUserDetails 객체로 변환
            .orElseThrow(() -> new UsernameNotFoundException(username + " -> DB에서 찾을 수 없습니다."));
            // 조회된 회원이 없을 경우 예외 발생
  }

  // 해당하는 User의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
  private UserDetails createUserDetails(Member member) {
    // 회원 권한 정보를 GrantedAuthority로 변환하여 User 객체 생성
    // GrantedAutority 객체 : Spring Security에서 권한을 표현하는 인터페이스
    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

    return new User( // 회원 id, 비밀번호, 권한 정보를 포함하는 User 객체
            String.valueOf(member.getId()), // 회원 id 값을 문자열로 변환
            member.getPassword(),
            Collections.singleton(grantedAuthority)
            // grantedAutority 객체를 단일 원소를 가진 Set으로 생성하여 권한 정보 설정
            /* Spring Security에서 사용자 권한 정보를 GrantedAutority 객체의 컬렉션으로 관리
            User 객체의 생성자에서 권한 정보를 Collection<? extends GrantedAuthority> 타입으로 받음
            여러개의 다양한 권한 정보를 가질 수 있는데, 단일 권한을 가진 사용자를 컬렉션으로 표현하기 위해 단일 원소를 가진 Set으로 생성 */
    );
  }
}
