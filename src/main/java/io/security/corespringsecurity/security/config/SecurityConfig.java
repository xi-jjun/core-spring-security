package io.security.corespringsecurity.security.config;

import io.security.corespringsecurity.security.handler.CustomAccessDeniedHandler;
import io.security.corespringsecurity.security.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//	private final UserDetailsService userDetailsService; // 안씀 => Provider 쓰기 때문.
	private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
	private final AuthenticationSuccessHandler authenticationSuccessHandler;
	private final AuthenticationFailureHandler authenticationFailureHandler;

	/**
	 * 우리가 만든 CustomAuthenticationProvider class 가
	 * UserDetailsService 를 받아서 쓰고 있기 때문에 원래 있던 configure method 대신 아래
	 * method 로 변경한 것.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	/**
	 * Spring Security 가 우리가 만든 CustomAuthenticationProvider 를 참조해서 검증하게 된다.
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new CustomAuthenticationProvider();
	}

//	/**
//	 * 이렇게 하면, Spring Security 는 우리가 만든 UserDetailsService 구현체를 사용하여
//	 * 인증처리를 진행하게 된다.

//   * 우리가 만든 CustomAuthenticationProvider class 가
//	 * UserDetailsService 를 받아서 쓰고 있기 때문에 원래 있던 configure method 대신 아래
//	 * method 로 변경한 것.
//	 */
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userDetailsService);
//	}

	/**
	 * 사용자 만들어서 각각 권한 부여하는 method
	 * 메모리 방식으로 인증처리하는 방법. => DB 로 할거니깐 주석처리함.
	 */
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		String password = passwordEncoder().encode("1111");
////		String password = "1111"; // 이 코드는 오류가 난다.
//
//		/**
//		 * "user" 라는 사용자의 비번은 password 이고, 권한은 "USER" 인 사용자를 생성하겠다.
//		 * 현재 3명의 사용자 생성. 각각 다른 권한을 부여함.
//		 *
//		 * password 는 암호화된 방식으로 줘야 오류를 안뱉는다.
//		 */
//		auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
//		auth.inMemoryAuthentication().withUser("manager").password(password).roles("MANAGER", "USER");
//		// admin 이라는 사용자에게 ADMIN, USER, MANAGER 라는 권한을 부여함. 따라서 3가지에 해당하는 권한을 쓸 수 있는 것이다.
//		auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN", "USER", "MANAGER");
////		auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN");
//		/**
//		 * ADMIN 이 더 높은지 컴퓨터는 모른다
//		 */
//
//		/**
//		 * 권한이 없는 url 에 접근하면 403 error
//		 * 403 ERROR (wiki) : HTTP 403, 403 Forbidden 은 서버가 허용하지 않는 웹 페이지나 미디어를 사용자가 요청할 때
//		 * 웹 서버가 반환하는 HTTP 상태 코드
//		 */
//	}

	/**
	 * literal 하게 선언된 문자열이 암호화가 된다.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	/**
	 * WebIgnore 설정 하기
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		// static files 는 보안 필터 안거치고 바로 통과됨.
		// 비용적인 면에서 permitAll() method 보다는 좋을 수 있다.
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

		/**
		 * FilterSecurityInterceptor.java line:113 에 suspend 걸어서 debug 툴로 보도록 해보자. => 안됨... 왜 안되는지 파악 못함
		 * 현재 method 가 존재하지 않는다면 "/" 과 같은 url 요청 때 static file(JS, CSS...) 에 대한 검사를 수행하는 것을 볼 수 있다.
		 */
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/", "member/login/**", "/login*", "/members", "/error").permitAll() // 보안 필터의 검사를 받는다. 단지 그 결과가 모든 접근에 대한 허락일 뿐이다.
				// 그러나 ignoring 은 필터 자체를 거치지 않는 것이다.
				.antMatchers("/my-page").hasRole("USER") // "/my-page" 경로에 USER 라는 권한을 가졌다면 접근 가능하게 한다.
				/**
				 * 우리가 Entity 에서 role field 를 ROLE_USER 로 했는데 여기서 USER, MANAGER 등등으로 하는 이유
				 * Params:
				 * role – the role that should be required which is prepended with ROLE_ automatically (i.e. USER, ADMIN, etc).
				 * It should not start with ROLE_
				 */
				.antMatchers("/manager").hasRole("MANAGER")
				.antMatchers("/config").hasRole("ADMIN")
				.anyRequest().authenticated()

		.and()
				.formLogin()
				// 우리가 만든 custom login page 를 추가하기 위한 코드들
				.loginPage("/login") // login
				.loginProcessingUrl("/login_proc") // login.html 의 action 을 보면 login_proc 이라고 되어 있다.
				/**
				 * 인증 처리 과정 속에서 2개의 클래스를 통해 전달되는 파라미터를 설정하고 저장하는 작업... 이라고 강의에서 나옴... 이해 안감
				 */
				.authenticationDetailsSource(authenticationDetailsSource)
				.defaultSuccessUrl("/") // login 성공 => root 로 이동
				.successHandler(authenticationSuccessHandler) // 우리가 만든 handler 가 인증이 성공하게 되면 호출되게 해준다.
				.failureHandler(authenticationFailureHandler)
				.permitAll() // 로그인은 인증 받지 못한 사용자들도 쓸 수 있어야 하기 때문이다.
		.and()
				.exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler()) // 우리가 만든 CustomAccessDeniedHandler 를 사용하기 위해 설정해주는 코드
		;
	}

	/**
	 * Bean 으로 등록하여 스프링이 관리할 수 있게 해준다.
	 */
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
		accessDeniedHandler.setErrorPage("/denied");
		return accessDeniedHandler;
	}
}
