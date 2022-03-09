package io.security.corespringsecurity.domain;

import lombok.Data;

@Data
public class MemberDTO {
	/**
	 * html 의 name attribute 랑 field name 이랑 같아야 매핑이 된다.
	 */
	private String name;
	private String id;
	private String password;
	private String nickname;
	private int age;
}
