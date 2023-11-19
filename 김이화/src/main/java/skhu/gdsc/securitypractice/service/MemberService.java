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
public class MemberService { // 회원 관련 비즈니스 로직을 처리하는 서비스 객체

  private final AuthenticationManagerBuilder authenticationManagerBuilder; // 인증 관련 기능을 제공하는 객체
  private final MemberRepository memberRepository; // 회원 정보 저장 및 조회에 사용
  private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 및 검증하기 위한 객체
  private final TokenProvider tokenProvider; // jwt 토큰 생성 및 검증을 위한 객체

  @Transactional
  public MemberResponseDto signup(MemberRequestDto memberRequestDto) { // 회원 가입
    if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
      throw new RuntimeException("이미 가입되어 있는 유저입니다"); // 동일한 이메일로 가입했을 경우 예외 발생
    }

    Member member = memberRequestDto.toMember(passwordEncoder); // 요청된 회원 정보를 기반으로 Member 객체 생성, 비밀번호 암호화
    return MemberResponseDto.of(memberRepository.save(member)); // 생성된 회원 정보를 데이터베이스에 저장하고 MemberResponseDto로 변환하여 반환
  }

  @Transactional
  public MemberResponseDto adminSignup(MemberRequestDto memberRequestDto) { // 회원 권한이 ROLE_ADMIN. 관리자 회원 가입
    if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
      throw new RuntimeException("이미 가입되어 있는 유저입니다");
    }

    Member member = memberRequestDto.toAdmin(passwordEncoder);
    return MemberResponseDto.of(memberRepository.save(member));
  }

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
