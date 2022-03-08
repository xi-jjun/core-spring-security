package io.security.corespringsecurity.repository;

import io.security.corespringsecurity.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberRepoImpl implements MemberRepository {
	private final EntityManager em;

	@Override
	@Transactional
	public Member save(Member member) {
		em.persist(member);
		return member;
	}

	@Override
	@Transactional
	public void remove(Long idx) {
		Member member = findOne(idx);
		em.remove(member);
	}

	@Override
	public List<Member> findAll() {
		return em.createQuery("select m from Member m", Member.class)
				.getResultList();
	}

	@Override
	public Member findOne(Long idx) {
		return em.find(Member.class, idx);
	}
}
