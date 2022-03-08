package io.security.corespringsecurity.domain;

import lombok.Data;

@Data
public class MemberDTO {
	private String name;
	private String id;
	private String password;
	private String nickname;
	private int age;
}
