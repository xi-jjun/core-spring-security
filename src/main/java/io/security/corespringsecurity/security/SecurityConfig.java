package io.security.corespringsecurity.security;

import jdk.internal.dynalink.support.NameCodec;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 사용자 만들어서 각각 권한 부여하는 method
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		String password = passwordEncoder().encode("1111");
//		String password = "1111"; // 이 코드는 오류가 난다.

		/**
		 * "user" 라는 사용자의 비번은 password 이고, 권한은 "USER" 인 사용자를 생성하겠다.
		 * 현재 3명의 사용자 생성. 각각 다른 권한을 부여함.
		 *
		 * password 는 암호화된 방식으로 줘야 오류를 안뱉는다.
		 */
		auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
		auth.inMemoryAuthentication().withUser("manager").password(password).roles("MANAGER", "USER");
		// admin 이라는 사용자에게 ADMIN, USER, MANAGER 라는 권한을 부여함. 따라서 3가지에 해당하는 권한을 쓸 수 있는 것이다.
		auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN", "USER", "MANAGER");
//		auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN");
		/**
		 * ADMIN 이 더 높은지 컴퓨터는 모른다
		 */

		/**
		 * 권한이 없는 url 에 접근하면 403 error
		 * 403 ERROR (wiki) : HTTP 403, 403 Forbidden 은 서버가 허용하지 않는 웹 페이지나 미디어를 사용자가 요청할 때
		 * 웹 서버가 반환하는 HTTP 상태 코드
		 */
	}

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
				.authorizeHttpRequests()
				.antMatchers("/").permitAll() // 보안 필터의 검사를 받는다. 단지 그 결과가 모든 접근에 대한 허락일 뿐이다.
				// 그러나 ignoring 은 필터 자체를 거치지 않는 것이다.
				.antMatchers("/my-page").hasRole("USER") // "/my-page" 경로에 USER 라는 권한을 가졌다면 접근 가능하게 한다.
				.antMatchers("/manager").hasRole("MANAGER")
				.antMatchers("/config").hasRole("ADMIN")
				.anyRequest().authenticated()

		.and()
				.formLogin()
				;
	}
}
