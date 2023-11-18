package skhu.gdsc.securitypractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skhu.gdsc.securitypractice.domain.Member;
import skhu.gdsc.securitypractice.dto.MemberRequestDto;
import skhu.gdsc.securitypractice.dto.MemberResponseDto;
import skhu.gdsc.securitypractice.dto.TokenDto;
import skhu.gdsc.securitypractice.jwt.TokenProvider;
import skhu.gdsc.securitypractice.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService { // 회원 정보를 관리하는 MemberService 클래스

  private final AuthenticationManagerBuilder authenticationManagerBuilder; // 인증을 담당하는 AuthenticationManagerBuilder 객체
  private final MemberRepository memberRepository; // Member 객체를 관리하는 MemberRepository 인터페이스
  private final PasswordEncoder passwordEncoder; // 비밀번호를 암호화하는 PasswordEncoder 객체
  private final TokenProvider tokenProvider; // Jwt Token을 생성하는 TokenProvider 객체

  @Transactional
  public MemberResponseDto signup(MemberRequestDto memberRequestDto) { // 회원 가입
    if (memberRepository.existsByEmail(memberRequestDto.getEmail())) { // 회원 이메일로 회원 정보가 존재하는지 확인
      throw new RuntimeException("이미 가입되어 있는 유저입니다"); // 예외 발생
    }

    Member member = memberRequestDto.toMember(passwordEncoder); // 회원 정보를 저장
    return MemberResponseDto.of(memberRepository.save(member)); // 회원 정보를 저장
  }

  @Transactional
  public MemberResponseDto adminSignup(MemberRequestDto memberRequestDto) { // 회원 가입
    if (memberRepository.existsByEmail(memberRequestDto.getEmail())) { // 회원 이메일로 회원 정보가 존재하는지 확인
      throw new RuntimeException("이미 가입되어 있는 유저입니다"); // 예외 발생
    }

    Member member = memberRequestDto.toAdmin(passwordEncoder); // 회원 정보를 저장
    return MemberResponseDto.of(memberRepository.save(member)); // 회원 정보를 저장
  }

  @Transactional
  public TokenDto login(MemberRequestDto memberRequestDto) { // 로그인
    // 1. username + password 를 기반으로 Authentication 객체 생성
    UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

    // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
    // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    // 3. 인증 정보를 기반으로 JWT 토큰 생성
    TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

    return tokenDto; // Jwt Token을 생성
  }
}
