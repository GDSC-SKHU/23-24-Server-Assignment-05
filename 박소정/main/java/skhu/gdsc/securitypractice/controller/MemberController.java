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

@RestController // RESTful API를 처리하는 컨트롤러임을 나타내는 어노테이션, 메서드의 반환 값은 HTTP 응답으로 직렬화되어 클라이언트에게 전송
@RequiredArgsConstructor // 필드를 초기화하는 생성자를 자동으로 생성
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
  public ResponseEntity<String> user() {
    return ResponseEntity.ok("Hello User");
  }

  @GetMapping("/admin")
  public ResponseEntity<String> admin() {
    return ResponseEntity.ok("Hello Admin");
  }
}

// Q. 엔드포인트가 아닌 API가 있나?
