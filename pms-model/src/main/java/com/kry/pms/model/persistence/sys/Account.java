package com.kry.pms.model.persistence.sys;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_account")
public class Account extends PersistenceModel {
	@Column
	private String nickname;
	@Column
	private String username;
	@Column
	private String mobile;
	@Column
	private String password;
	@Column(name="type_")
	private String type;
	@OneToMany
	private List<Role> roles;
}
