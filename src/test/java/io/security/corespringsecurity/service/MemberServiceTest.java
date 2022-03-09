package io.security.corespringsecurity.service;

import static org.junit.jupiter.api.Assertions.*;

import io.security.corespringsecurity.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class MemberServiceTest {
	@Autowired
	private MemberService memberService;

	@BeforeEach
	void makeDummy() {
		Member memberA = createMember("A", "AAA123", "1234", "AaaA", 26);
		Member memberB = createMember("B", "BBB123", "1234", "BbbB", 24);
		Member memberC = createMember("C", "CCC1234", "1234", "CccC", 20);
		memberService.join(memberA);
		memberService.join(memberB);
		memberService.join(memberC);
	}

	@Test
	void join() {
		Member toBeSaved = createMember("김재준", "rlawowns1234", "1234", "GongDoll", 26);

		memberService.join(toBeSaved);

		Member findMember = memberService.findMember("rlawowns1234");

		assertEquals(toBeSaved.getId(), findMember.getId());
		assertEquals(toBeSaved.getAge(), findMember.getAge());
		assertEquals(toBeSaved.getName(), findMember.getName());
		assertEquals(toBeSaved.getNickname(), findMember.getNickname());
	}

	@Test
	void findAll() {
		List<Member> members = memberService.findAll();
		for (Member member : members) {
			System.out.println("member = " + member);
		}

		assertEquals(3, members.size());
	}

	private Member createMember(String name, String id, String password, String nickname, int age) {
		Member toBeSaved = new Member();
		toBeSaved.setName(name);
		toBeSaved.setNickname(nickname);
		toBeSaved.setId(id);
		toBeSaved.setPassword(password);
		toBeSaved.setAge(age);
		return toBeSaved;
	}
}
