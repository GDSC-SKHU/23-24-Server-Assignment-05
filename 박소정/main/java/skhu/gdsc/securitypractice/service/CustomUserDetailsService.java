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
public class CustomUserDetailsService implements UserDetailsService {
  /*
  * UserDetailsService
  * 스프링 시큐리티에서 지원해주는 인터페이스
  * 인증에 필요한 UserDetailsService interface의 loadUserByUsername 메서드를 구현하는 것이 핵심
  * -> DB에 접근하여 사용자 정보를 가져옴
  * */

  private final MemberRepository memberRepository;


  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return memberRepository.findByEmail(username)
            .map(this::createUserDetails) // 메서드 참조를 사용하여 각 요소를 createUserDetails 메서드로 매핑, .map()은 스트림의 각 요소에 함수를 적용하여 새로운 요소로 변환하는 역할
            .orElseThrow(() -> new UsernameNotFoundException(username + " -> DB에서 찾을 수 없습니다."));
  } // DB에 접근하여 사용자의 정보를 가져옴.

  // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
  private UserDetails createUserDetails(Member member) {
    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

    return new User(
            String.valueOf(member.getId()),
            member.getPassword(),
            Collections.singleton(grantedAuthority) // 싱글톤 패턴: 객체의 인스턴스가 오직 1개만 생성. 프로그램 내에서 하나로 공유를 해야하는 객체가 존재할 때 해당 객체를 싱글톤으로 구현
    );
  }
}
