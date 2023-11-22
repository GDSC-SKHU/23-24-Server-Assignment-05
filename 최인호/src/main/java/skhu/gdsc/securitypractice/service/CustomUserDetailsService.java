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
public class CustomUserDetailsService implements UserDetailsService { // UserDetailsService 인터페이스 구현

  private final MemberRepository memberRepository;


  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 사용자 정보 불러오기.
    return memberRepository.findByEmail(username)// username을 통해서 멤버를 찾고
            .map(this::createUserDetails) // 찾은 멤버값으로 createUserDetails 메서드를 수행해 UserDetails 객체로 변환.
            .orElseThrow(() -> new UsernameNotFoundException(username + " -> DB에서 찾을 수 없습니다."));  // 없으면 예외처리.
  }

  private UserDetails createUserDetails(Member member) { // Member를 UserDetails로 반환하는 메서드
    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());
    // 사용자 권환을 GrantedAuthority 객체로 생성하고

    return new User( // UserDetails 객체를 생성해서 반환.
            String.valueOf(member.getId()), // User 객체는 사용자의 아이디, 비밀번호, 권한 정보를 가지고 있음.
            member.getPassword(),
            Collections.singleton(grantedAuthority)
    );
  }
}
