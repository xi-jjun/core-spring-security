package io.security.corespringsecurity.service;

import io.security.corespringsecurity.domain.Member;
import io.security.corespringsecurity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	/**
	 * 원래는 중복되는 id 와 nickname 을 막아야 하지만 이 프로젝트의 목적은
	 * 서비스가 아니라 spring security 에 대해 배우는게 목적이기에 생략.
	 */
	public void join(Member member) {
		memberRepository.save(member);
	}

	@Override
	public Member findMember(String id) {
		return memberRepository.findAll().stream()
				.filter(m -> m.getId().equals(id))
				.findFirst().orElse(null);
	}

	@Override
	public Member findMember(Long idx) {
		return memberRepository.findOne(idx);
	}

	@Override
	@Transactional
	public void remove(Long idx) {
		memberRepository.remove(idx);
	}

	@Override
	public List<Member> findAll() {
		return memberRepository.findAll();
	}
}
