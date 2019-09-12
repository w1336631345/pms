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
	@OneToOne
	private Department department;
	@Column(columnDefinition = "varchar(64) default NULL COMMENT '名称'")
	private String name;
	@Column(columnDefinition = "varchar(40) default NULL COMMENT '编码'")
	private String code;
	@Column(columnDefinition = "varchar(400) default NULL COMMENT '描述'")
	private String description;
	@OneToOne
	private Account account;
}
