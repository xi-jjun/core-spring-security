package io.security.corespringsecurity.security.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String errorMessage = "Invalid username or password"; // default error message

		/**
		 * 이런 예외들은 UserDetailsService 의 구현체(우리가 구현한 것) or
		 * Provider 에서 던진 예외를 잡는 방식으로...
		 */
		if (exception instanceof BadCredentialsException) {
			errorMessage = "Invalid username or password";
		} else if (exception instanceof InsufficientAuthenticationException) {
			errorMessage = "Invalid Secret key";
		}

		// 해당 예외를 LoginController 에서 param 같은 것을 받아서 처리할 수 있게 해주자
		// "/login?error=true&exception" => 접근을 허용해줘야 함
		setDefaultFailureUrl("/login?error=true&exception=" + exception.getMessage()); // error param 던지게

		// 응답 처리를 부모에게 위임
		super.onAuthenticationFailure(request, response, exception);
	}
}
