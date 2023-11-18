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

  // 유저 등록
  @PostMapping("/signup/user")
  public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
    // 결과는 ResponseEntity에 담아서 반환
    return ResponseEntity.ok(memberService.signup(memberRequestDto));
  }

  // 관리자 등록
  @PostMapping("/signup/admin")
  public ResponseEntity<MemberResponseDto> adminSignup(@RequestBody MemberRequestDto memberRequestDto) {
    return ResponseEntity.ok(memberService.adminSignup(memberRequestDto));
  }

  // 로그인 등록
  @PostMapping("/login")
  public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto memberRequestDto) {
    return ResponseEntity.ok(memberService.login(memberRequestDto));
  }

  // 유저 조회
  @GetMapping("/user")
  public ResponseEntity<String> user() {
    return ResponseEntity.ok("Hello User");
  }

  // 관리자 조회
  @GetMapping("/admin")
  public ResponseEntity<String> admin() {
    return ResponseEntity.ok("Hello Admin");
  }
}
