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

  @PostMapping("/signup/user")
  public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
    return ResponseEntity.ok(memberService.signup(memberRequestDto));
  }

  @PostMapping("/signup/admin")
  public ResponseEntity<MemberResponseDto> adminSignup(@RequestBody MemberRequestDto memberRequestDto) {
    return ResponseEntity.ok(memberService.adminSignup(memberRequestDto));
  }

  @PostMapping("/login")
  public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto memberRequestDto) {
    return ResponseEntity.ok(memberService.login(memberRequestDto));
  }

  @GetMapping("/user")
  public ResponseEntity<String> user() { // ResponseEntity는 스프링에서 응답을 나타내는 클래스
    return ResponseEntity.ok("Hello User");  //HTTP 상태 코드 200(OK)와 함께 Hello User를 응답으로 반환
  }

  @GetMapping("/admin")
  public ResponseEntity<String> admin() {   // ResponseEntity는 스프링에서 응답을 나타내는 클래스
    return ResponseEntity.ok("Hello Admin");   //HTTP 상태 코드 200(OK)와 함께 Hello Admin 응답으로 반환
  }
}
