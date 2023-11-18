package Security.example.demo.service;

import Security.example.demo.domain.Member;
import Security.example.demo.dto.MemberRequestDto;
import Security.example.demo.dto.MemberResponseDto;
import Security.example.demo.dto.TokenDto;
import Security.example.demo.jwt.TokenProvider;
import Security.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 화원가입 및 로그인 기능
@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;  // 비밀번호 암호화
    private final TokenProvider tokenProvider;  // 토큰 생성

    // 회원가입
    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {  // 이미 가입된 이메일이 있는지 확인하고 있으면 예외처리
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }
    // 관리자 회원가입
    @Transactional
    public MemberResponseDto adminSignup(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) { // 이미 가입된 이메일이 있는지 확인하고 있으면 예외처리
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = memberRequestDto.toAdmin(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    // 로그인
    @Transactional
    public TokenDto login(MemberRequestDto memberRequestDto) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        return tokenDto;
    }
}
