package io.security.corespringsecurity.repository;

import static org.junit.jupiter.api.Assertions.*;

import io.security.corespringsecurity.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class MemberRepoImplTest {
	@Autowired
	private MemberRepository memberRepository;

	@BeforeEach
	void makeDummy() {
		Member memberA = createMember("A", "AAA123", "1234", "AaaA", 26);
		Member memberB = createMember("B", "BBB123", "1234", "BbbB", 24);
		Member memberC = createMember("C", "CCC1234", "1234", "CccC", 20);
		memberRepository.save(memberA);
		memberRepository.save(memberB);
		memberRepository.save(memberC);
	}

	@Test
	@DisplayName("repo save test")
	void save() {
		// given
		Member toBeSaved = createMember("김재준", "rlawowns1234", "1234", "GongDoll", 26);

		// when
		memberRepository.save(toBeSaved);

		// then
		List<Member> members = memberRepository.findAll();
		Member findMember = new Member();
		for (Member member : members) {
			if (member.getId().equals(toBeSaved.getId())) {
				findMember = member;
			}
		}
		assertEquals(toBeSaved.getId(), findMember.getId());
		assertEquals(toBeSaved.getAge(), findMember.getAge());
		assertEquals(toBeSaved.getName(), findMember.getName());
		assertEquals(toBeSaved.getNickname(), findMember.getNickname());
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

	@Test
	@DisplayName("findAll Test")
	void findAll() {
		// given
		Member member = createMember("FINAL_MEMBER", "fi", "123", "f-ial", 16);
		memberRepository.save(member);

		// when
		List<Member> members = memberRepository.findAll();
		Member findOne = memberRepository.findOne((long) members.size());

		// then
		assertEquals(member, findOne); // idx check
		for (Member mem : members) {
			System.out.println("mem = " + mem);
		}
	}
}
