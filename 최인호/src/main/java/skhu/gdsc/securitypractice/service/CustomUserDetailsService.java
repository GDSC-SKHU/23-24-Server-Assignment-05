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
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // username을 가져와서
    return memberRepository.findByEmail(username)// 그걸로 사용자를 찾고 찾지 못한다면 예외를 터트림.
            .map(this::createUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException(username + " -> DB에서 찾을 수 없습니다."));
  }

  private UserDetails createUserDetails(Member member) { // Member를 UserDetails로 반환하는 메서드
    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());
    // 사용자 권환을 GrantedAuthority 객체로 생성하고

    return new User( // UserDetails 객체를 생성해서 반환.
            String.valueOf(member.getId()),
            member.getPassword(),
            Collections.singleton(grantedAuthority)
    );
  }
}
