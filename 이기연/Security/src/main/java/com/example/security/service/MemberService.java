package com.example.security.service;

import com.example.security.domain.Member;
import com.example.security.dto.MemberRequestDto;
import com.example.security.dto.MemberResponseDto;
import com.example.security.dto.TokenDto;
import com.example.security.jwt.TokenProvider;
import com.example.security.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    // 스프링 시큐리티의 AuthenticationManagerBuilder를 주입받음
    private final MemberRepository memberRepository;
    // Member 엔터티에 대한 데이터베이스 작업을 수행하는 리포지토리
    private final PasswordEncoder passwordEncoder;
    // 비밀번호를 암호화하기 위한 PasswordEncoder
    private final TokenProvider tokenProvider;
    // 토큰 생성 및 검증을 수행하는 TokenProvider

    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        // 회원 가입을 처리하는 메서드
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            // 이미 해당 이메일로 가입된 멤버가 있는 경우 예외를 던짐
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        // MemberRequestDto를 사용하여 Member 엔터티를 생성
        return MemberResponseDto.of(memberRepository.save(member));
        // 생성된 멤버를 저장하고, 저장된 멤버를 MemberResponseDto로 변환하여 반환
    }

    @Transactional
    // 트랜잭션을 사용하여 데이터베이스 작업을 수행
    public MemberResponseDto adminSignup(MemberRequestDto memberRequestDto) {
        // 관리자 계정으로 회원 가입을 처리하는 메서드
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            // 이미 해당 이메일로 가입된 멤버가 있는 경우 예외를 던짐
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = memberRequestDto.toAdmin(passwordEncoder);
        // MemberRequestDto를 사용하여 관리자 권한을 갖는 Member 엔터티를 생성
        return MemberResponseDto.of(memberRepository.save(member));
        // 생성된 멤버를 저장하고, 저장된 멤버를 MemberResponseDto로 변환하여 반환
    }

    @Transactional
    // 트랜잭션을 사용하여 데이터베이스 작업을 수행
    public TokenDto login(MemberRequestDto memberRequestDto) {
        // 로그인을 처리하는 메서드

        // 1. username + password를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        return tokenDto;
        // 생성된 토큰을 반환
    }
}
