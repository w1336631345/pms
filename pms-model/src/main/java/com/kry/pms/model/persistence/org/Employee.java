package com.kry.pms.model.persistence.org;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.sys.Account;

import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "t_employee")
public class Employee extends PersistenceModel{
	@Column
	private String name;
	@OneToOne
	private Department department;
	@Column
	private String code;
	@Column
	private String description;
	@OneToOne
	private Account account;
}
