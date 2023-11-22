package skhu.gdsc.securitypractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import skhu.gdsc.securitypractice.dto.MemberRequestDto;
import skhu.gdsc.securitypractice.dto.MemberResponseDto;
import skhu.gdsc.securitypractice.dto.TokenDto;
import skhu.gdsc.securitypractice.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;

  @PostMapping("/signup/user") //회원가입
  public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
    return ResponseEntity.ok(memberService.signup(memberRequestDto));
  }

  @PostMapping("/signup/admin") //관리자 회원가입
  public ResponseEntity<MemberResponseDto> adminSignup(@RequestBody MemberRequestDto memberRequestDto) {
    return ResponseEntity.ok(memberService.adminSignup(memberRequestDto));
  }

  @PostMapping("/login") //로그인
  public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto memberRequestDto) {
    return ResponseEntity.ok(memberService.login(memberRequestDto));
  }

  @GetMapping("/user") //로그인 성공하면 인증 토큰 생성
  public ResponseEntity<String> user() {
    return ResponseEntity.ok("Hello User");
  }

  @GetMapping("/admin") //로그인 성공하면 인증 토큰 생성
  public ResponseEntity<String> admin() {
    return ResponseEntity.ok("Hello Admin");
  }
}
