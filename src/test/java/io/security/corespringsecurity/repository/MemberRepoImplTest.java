package io.security.corespringsecurity.repository;

import static org.junit.jupiter.api.Assertions.*;

import io.security.corespringsecurity.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
class MemberRepoImplTest {
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private EntityManager em;

	@Test
	@DisplayName("repo save test")
	void save() {
		// given
		Member toBeSaved = new Member();
		toBeSaved.setName("김재준");
		toBeSaved.setAge(26);
		toBeSaved.setNickname("공돌이");
		toBeSaved.setId("rlawowns1234");
		toBeSaved.setPassword("12345");

		// when
		Member save = memberRepository.save(toBeSaved);

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
}
