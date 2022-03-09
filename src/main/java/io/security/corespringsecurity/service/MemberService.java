package io.security.corespringsecurity.service;

import io.security.corespringsecurity.domain.Member;

import java.util.List;

public interface MemberService {
	public void join(Member member);

	public Member findMember(String id);

	public Member findMember(Long idx);

	public void remove(Long idx);

	public List<Member> findAll();
}
