package io.security.corespringsecurity.security.service;

import io.security.corespringsecurity.domain.Member;
import io.security.corespringsecurity.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService") // 우리가 만든 이 클래스를 Bean 으로 설정!! SecurityConfig.java 의 field 에 들어갈 수 있게하기 위함.
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {
	private final MemberService memberService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// data layer 로 부터 member 객체에 대한 정보를 불러오는 코드
		Member member = memberService.findMember(username); // 내 entity 에서는 id 가 username 이다.

		if (member == null) {
			throw new UsernameNotFoundException("User name Not Founded Exception Occurred");
		}

		List<GrantedAuthority> roles = new ArrayList<>();
//		roles.add(new SimpleGrantedAuthority("ROLE_USER"));
		roles.add(new SimpleGrantedAuthority(member.getRole()));

		MemberContext memberContext = new MemberContext(member, roles); // (member, 권한정보)

		// (MemberContext extends User), User implements UserDetails
		return memberContext;
	}
}
