package com.kry.pms.model.persistence.org;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.model.persistence.sys.Role;
import com.kry.pms.model.persistence.sys.User;

import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "t_employee")
public class Employee extends PersistenceModel{
	@NotNull(message="必须选择部门")
	@OneToOne
	private Department department;
	@NotBlank
	@Column(columnDefinition = "varchar(64) default NULL COMMENT '名称'")
	private String name;
	@NotBlank
	@Column(columnDefinition = "varchar(40) default NULL COMMENT '编码'")
	private String code;
	@Column(columnDefinition = "varchar(400) default NULL COMMENT '描述'")
	private String description;
	@NotBlank(message="电话号码不为空")
	@Column(columnDefinition = "varchar(40) default NULL COMMENT '电话'")
	private String mobile;
	@Column
	private String type;
	@OneToOne
	private User user;
	@ManyToOne
	private Role role;
}
