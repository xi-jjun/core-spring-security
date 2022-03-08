package io.security.corespringsecurity.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data // getter, setter
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;
	private String name;
	private String id;
	private String password;
	private String nickname;
	private int age;

}
