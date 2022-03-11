package io.security.corespringsecurity.controller.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
						@RequestParam(value = "exception", required = false) String exception, Model model) {
		model.addAttribute("error", error);
		model.addAttribute("exception", exception);

		return "member/login/login";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		// 인증 객체를 받아오는 코드
		/**
		 * logout 을 한다는 것 => 이미 인증을 받은 상태. 따라서 인증 객체가 SecurityContext 안에 있다.
		 * 따라서 해당 인증을 logout 하기 위해 객체를 가져와야 하는 것
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null) {
			// logout
			new SecurityContextLogoutHandler().logout(request, response, authentication); // request, res, 인증을 받은 사용자의 인증 객체
		}

		return "redirect:/login";
	}
}
