package io.security.corespringsecurity.repository;

import io.security.corespringsecurity.domain.Member;

import java.util.List;

public interface MemberRepository {
	public void save(Member member);

	public void remove(Long idx);

	public Member findOne(Long idx);

	public List<Member> findAll();
}
