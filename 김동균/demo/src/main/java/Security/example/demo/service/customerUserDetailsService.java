package Security.example.demo.service;

import Security.example.demo.domain.Member;
import Security.example.demo.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class customerUserDetailsService implements UserDetailsService {  // UserDetailsService는 Spring Security에서 유저의 정보를 가져오는 인터페이스이다.

    private final MemberRepository memberRepository;

    // 유저의 정보를 불러와서 UserDetails로 리턴
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)  // 이름을 통해 이메일 찾기
                .map(this::createUserDetails)  // UserDetail객체로 변환
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> DB에서 찾을 수 없습니다."));  // 이름이 없으면 예외처리
    }

    // 해당하는 User의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
    private UserDetails createUserDetails(Member member) {
        // 권한 객체 생성
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new User(  // User객체 반환
                String.valueOf(member.getId()),  // 아이디
                member.getPassword(),  // 비밀번호
                Collections.singleton(grantedAuthority)  // 권한정보
        );
    }
}
