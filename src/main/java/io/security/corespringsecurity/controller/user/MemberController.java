package io.security.corespringsecurity.controller.user;

import io.security.corespringsecurity.domain.Member;
import io.security.corespringsecurity.domain.MemberDTO;
import io.security.corespringsecurity.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private final PasswordEncoder passwordEncoder; // password 와 같은 정보를 암호화 하기 위함
	private final MemberService memberService;

	@GetMapping("/my-page")
	public String myPage() {
		return "member/my-page";
	}

	@GetMapping("/members")
	public String createMember() {
		return "member/login/memberForm";
	}

	@PostMapping("/members")
	public String createMember(MemberDTO memberDTO) {
		ModelMapper mapper = new ModelMapper();
		Member member = mapper.map(memberDTO, Member.class);
		member.setPassword(passwordEncoder.encode(member.getPassword())); // password 암호화

		memberService.join(member); // 회원 가입

		return "redirect:/";
	}
}
