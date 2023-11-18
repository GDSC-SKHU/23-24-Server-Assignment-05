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

  @PostMapping("/signup/user") // 회원가입 API
  public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) { // 회원가입 요청을 받아서 회원가입 로직을 수행
    return ResponseEntity.ok(memberService.signup(memberRequestDto)); // 회원가입 로직을 수행한 결과를 반환
  }

  @PostMapping("/signup/admin") // 관리자 회원가입 API
  public ResponseEntity<MemberResponseDto> adminSignup(@RequestBody MemberRequestDto memberRequestDto) { // 관리자 회원가입 요청을 받아서 관리자 회원가입 로직을 수행
    return ResponseEntity.ok(memberService.adminSignup(memberRequestDto)); // 관리자 회원가입 로직을 수행한 결과를 반환
  }

  @PostMapping("/login") // 로그인 API
  public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto memberRequestDto) { // 로그인 요청을 받아서 로그인 로직을 수행
    return ResponseEntity.ok(memberService.login(memberRequestDto)); // 로그인 로직을 수행한 결과를 반환
  }

  @GetMapping("/user") // 유저 API
  public ResponseEntity<String> user() {
    return ResponseEntity.ok("Hello User");
  } // 유저 API는 누구나 접근 가능

  @GetMapping("/admin") // 관리자 API
  public ResponseEntity<String> admin() {
    return ResponseEntity.ok("Hello Admin");
  } // 관리자 API는 관리자 권한을 가진 사람만 접근 가능
}
