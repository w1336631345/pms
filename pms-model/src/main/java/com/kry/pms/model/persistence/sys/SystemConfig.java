package com.kry.pms.model.persistence.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name="t_function")
public class SystemConfig extends PersistenceModel{
	@Column
	private String name;
	@Column(name="key_")
	private String key;
	@Column(name="value_")
	private String value;
	@Column(name="type_")
	private String type;
	@Column(name="version_")
	private Integer version;
	@Column(name="group_")
	private String group;
	@Column(name="group_key")
	private String groupKey;
	@Column
	private boolean currentVersion = true;
	@Column
	private String description;
	@Column
	private String defaultValue;

}
