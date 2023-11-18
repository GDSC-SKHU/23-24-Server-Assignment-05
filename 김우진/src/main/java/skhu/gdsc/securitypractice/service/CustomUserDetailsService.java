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

  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //사용자 이름을 통해서 사용자 인증 정보를 조회하는 메서드
    return memberRepository.findByEmail(username) //사용자 이름을 받아서 그와 맞는 이메일로 DB에서 조회함
            .map(this::createUserDetails) //사용자 정보가있으면 createUserDetails를 통해서 userdetail 객체를 생성하고 반환
            .orElseThrow(() -> new UsernameNotFoundException(username + " -> DB에서 찾을 수 없습니다.")); //없으면 이 이름은 없어~ 하고 알려줘~
  }

  // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
  private UserDetails createUserDetails(Member member) { //
    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString()); //사용자의 권한을 문자열로 grantedAuthority에 저장해

    return new User(
            String.valueOf(member.getId()), //UserDetail에 멤버의 id를 문자열로 반환
            member.getPassword(), //user 비밀번호 호출
            Collections.singleton(grantedAuthority) // 권한 정보를 singleton을 사용해서 단일 권한 정보를 가지는 컬렉션을 User 정보에 저장~
    );
  }
}
