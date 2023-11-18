package com.example.security.service;

import com.example.security.domain.Member;
import com.example.security.repository.MemberRepository;
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

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    // Member 엔터티에 대한 데이터베이스 작업을 수행하는 리포지토리

    @Override
    @Transactional
    // 트랜잭션을 사용하여 데이터베이스에서 사용자 정보를 로드
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 이름을 기반으로 사용자 정보를 로드하는 메서드
        return memberRepository.findByEmail(username)
                // 이메일을 사용하여 멤버를 찾음
                .map(this::createUserDetails)
                // 멤버가 존재하면 UserDetails로 변환
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> DB에서 찾을 수 없습니다."));
        // 멤버가 존재하지 않으면 예외를 던짐
    }

    // 해당하는 User의 데이터가 존재한다면 UserDetails 객체로 만들어서 반환
    private UserDetails createUserDetails(Member member) {
        // 멤버 정보를 사용하여 UserDetails 객체를 생성하는 메서드

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());
        // 멤버의 권한 정보를 GrantedAuthority로 변환

        return new User(
                String.valueOf(member.getId()),
                // 사용자의 ID를 문자열로 변환하여 설정
                member.getPassword(),
                // 사용자의 비밀번호를 설정
                Collections.singleton(grantedAuthority)
                // 권한 정보를 담은 Set을 생성하여 설정
        );
    }
}
