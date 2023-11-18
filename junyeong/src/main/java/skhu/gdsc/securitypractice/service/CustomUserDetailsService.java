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
public class CustomUserDetailsService implements UserDetailsService { // UserDetailsService 인터페이스를 구현한 CustomUserDetailsService 클래스

  private final MemberRepository memberRepository; // Member 객체를 관리하는 MemberRepository 인터페이스


  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // username 을 받아서 UserDetails 객체를 return
    return memberRepository.findByEmail(username) // username 을 받아서 UserDetails 객체를 return
            .map(this::createUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException(username + " -> DB에서 찾을 수 없습니다."));
  }

  private UserDetails createUserDetails(Member member) { // Member 객체를 UserDetails 객체로 변환
    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString()); // Member 객체의 권한 정보를 가져와서 GrantedAuthority 객체로 변환

    return new User( // UserDetails 객체를 생성
            String.valueOf(member.getId()), // UserDetails 객체를 생성
            member.getPassword(), // UserDetails 객체를 생성
            Collections.singleton(grantedAuthority) // UserDetails 객체를 생성
    );
  }
}
