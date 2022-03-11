package io.security.corespringsecurity.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	// 이전에 사용자가 거쳐왔던 요청에 관련된 정보에 관련된 객체를 꺼내와서 참고하기 위한 객체
	private RequestCache requestCache = new HttpSessionRequestCache();

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	/**
	 * 인증 성공 후 여러가지 후속작업 가능.
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		setDefaultTargetUrl("/"); // 기본적으로 설정해놓는 url

		// 사용자가 인증에 성공하기 전에 요청했던 정보를 담고 있는 객체.
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		/**
		 * 1. 이전에 접근할 수 없는 자원에 접근했다가 다시 로그인 페이지로 와서 로그인했을 때, => 인증에 예외가 생긴 후 로그인에 성공할 때를 말하는 것
		 * 2. 이전에 요청한 적이 없을 때
		 * -> 요청이 없었기에 savedRequest 객체는 null 이된다. => 생성이 안된다고 한다.
		 * ==> null check 하는 이유
		 */

		if (savedRequest != null) {
			String targetUrl = savedRequest.getRedirectUrl(); // 우리가 가고자 했던 url
			redirectStrategy.sendRedirect(request, response, targetUrl); // 우리가 가고자 했던 url 로 이동하게 해준다.
		} else { // default page
			redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl()); // go default target url
		}
	}
}
