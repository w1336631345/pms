package com.kry.pms.model.persistence.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_sql_template")
public class SqlTemplate extends PersistenceModel {
	@Column
	private String entityName;
	@Column
	private String methodName;
	@Column
	private String sql;
}
