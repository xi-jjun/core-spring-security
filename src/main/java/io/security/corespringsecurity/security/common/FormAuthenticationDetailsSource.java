package io.security.corespringsecurity.security.common;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class FormAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
	@Override
	public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
		/**
		 * [실전! form 인증 8강 전체 흐름]
		 * 1. ID : user1, PW : 123 으로 로그인 요청!
		 *
		 * 2. UsernamePasswordAuthenticationFilter class 의 setDetails(..) method 로 details 에 값을 저장.
		 * 	authenticationDetailsSource.buildDetails(request) 를 생성하는 것을 위 클래스의 메서드에서 볼 수 있다.(아래 3줄)
		 *
		 * 	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		 * 		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
		 *  }
		 *
		 * 	그리고 UsernamePasswordAuthenticationToken class 의 부모 class 인 AbstractAuthenticationToken class 의 setDetails()
		 * 	method 호출하여 details 에 속성 저장. => authRequest 설명
		 *
		 * 	authenticationDetailsSource 는 AbstractAuthenticationProcessingFilter class 의 field 이다. 해당 field 는
		 * 	protected AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
		 * 	와 같이 생성된다.
		 *
		 * 	WebAuthenticationDetailsSource 는 AuthenticationDetailsSource 인터페이스의 구현체이다.
		 * 	근데? 우리가 만든 FormAuthenticationDetailsSource(현재클래스) 는 AuthenticationDetailsSource 를 구현한다.
		 * 	=> authenticationDetailsSource 는 결국 FormAuthenticationDetailsSource(현재클래스) 를 가리키게 된다.
		 *
		 * 3. 따라서 FormAuthenticationDetailsSource 가 FormWebAuthenticationDetails 를 생성해주고 return.
		 * request 객체도 같이 전달. 왜? id, pw, 추가정보 가 '우리가 서버에 요청한 데이터' 니깐.
		 * => AuthenticationDetailsSource 역할인 'WebAuthenticationDetails 의 생성' 을 했다.
		 *
		 * 4. CustomAutenticationProvider 에서 authentication 객체에서 getDetails() method 로 details 객체 가져오기.
		 * 가져올 때 FormWebAuthenticationDetails 로 casting.
		 *
		 * 5. 가져온 details 에서 추가정보 보면서 검증을 진행하면 됨.
		 * 현재 프로젝트에서는 login.html 에서 name="secret_key" value="secret" 인 details 정보를 줬음.
		 * 따라서 request.getParameter("secret_key") 가 secret 이랑 같다면 => 검증 통과
		 * %% details 가 있는 곳. <= 인증 객체 (authentication) 안이다.
		 * authentication
		 * ㄴ details
		 * 	  ㄴ secretKey
		 * 	  ㄴ remoteAddress
		 * 	  ㄴ sessionId
		 */
		return new FormWebAuthenticationDetails(context);
	}
}
