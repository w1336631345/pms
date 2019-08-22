package com.kry.pms.model.persistence.org;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name="t_department")
public class Department extends PersistenceModel{
	@Column
	private String name;
	@Column
	private String code;
	@Column
	private String description;
	@OneToOne
	private Corporation corporation;
}
