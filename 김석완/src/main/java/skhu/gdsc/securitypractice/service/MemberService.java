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
public class MemberService {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;

  @Transactional
  public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
    if (memberRepository.existsByEmail(memberRequestDto.getEmail())) { // 사용중인 이메일인지 확인
      throw new RuntimeException("이미 가입되어 있는 유저입니다");
    }

    Member member = memberRequestDto.toMember(passwordEncoder); // Member 엔터티 생성 및 저장
    return MemberResponseDto.of(memberRepository.save(member));
  }

  @Transactional
  public MemberResponseDto adminSignup(MemberRequestDto memberRequestDto) {
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
