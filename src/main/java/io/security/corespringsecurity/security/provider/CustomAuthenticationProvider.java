package io.security.corespringsecurity.security.provider;

import io.security.corespringsecurity.security.common.FormWebAuthenticationDetails;
import io.security.corespringsecurity.security.service.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * CustomMemberDetailsService 구현체가 반환하는 UserDetails 객체를 받아,
 * 추가적인 검증을 진행하는 class 인 CustomAuthenticationProvider 를 구현.
 */
// @RequiredArgsConstructor // SecurityConfig 에서 Bean 으로 등록 위해 기본 생성자 필요.
// 따라서 아래 field 를 @Autowired 로 받았음.
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	// 우리가 지난 시간에 만들었던 CustomMemberDetailsService 구현체 사용하기 위함.
	private UserDetailsService userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 검증을 위한 구현 Method
	 * @param authentication : authenticationManager 클래스로부터 전달 받는 인증객체.
	 *                       인증할 때 사용자가 입력한 ID, Password 정보가 있다.
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials(); // 사용자가 입력한 password

		// ID 검증
		MemberContext memberContext = (MemberContext) userDetailsService.loadUserByUsername(username);

		/**
		 * Password 검증 => ID 가 있다는 것이 위에서 확인됨. 따라서 비번을 검증할 차례
		 * 사용자가 입력한 password 와 MemberContext 의 password(DB 에 저장된 암호화된 비번) 이 동일해야 한다.
		 */
		if (!passwordEncoder.matches(password, memberContext.getMember().getPassword())) {
			throw new BadCredentialsException("Bad Credentials Error");
		}

		/**
		 * secret key 검증을 해볼 것임.
		 * id, pw 외에도 클라이언트에게 받은 데이터 중에
		 * details 로 부터 받은 정보랑 다르면 예외 발생시켜서 최종적인 인증 진행하는 것.
		 */
		FormWebAuthenticationDetails formWebAuthenticationDetails = (FormWebAuthenticationDetails) authentication.getDetails();
		String secretKey = formWebAuthenticationDetails.getSecretKey();
		/**
		 * "secret" : login.html 의 name="secret_key" 의 value 값이다.
		 * 해당 html 의 value 값이 우리가 지정한 "secret" 과 같을 경우 => 인증 통과.
		 */
		if (secretKey == null || !"secret".equals(secretKey)) {
			throw new InsufficientAuthenticationException("InsufficientAuthenticationException");
		}


		/**
		 * 최종적으로 인증에 성공한 인증 객체를 생성.
		 */
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				memberContext.getMember(),
				null,
				memberContext.getAuthorities() // memberContext 는 User 상속받았기에 해당 method 사용가능
				// 권한은 CustomMemberDetailsService 구현체에서  MemberContext 생성할 때 roles List 객체로 전달했었음 이미.
		);

		return authenticationToken;
	}

	/**
	 * param 의 type 과 현재 클래스가 사용하고자 하는 token 의 타입이 일치할 때
	 * 이 provider 가 인증처리를 할 수 있게 하기 위한 method
	 *
	 * @param authentication
	 * @return
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
