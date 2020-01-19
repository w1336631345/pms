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
@Table(name = "t_system_config")
public class SystemConfig extends PersistenceModel {
	@Column
	private String name;//名称
	@Column(name = "key_")
	private String key;//key
	@Column(name = "value_")
	private String value;//值
	@Column(name = "type_")
	private String type;//类型
	@Column(name = "group_")
	private String group;
	@Column(name = "group_key")
	private String groupKey;//分组
	@Column
	private Boolean useOnWeb;//是否发给前端
	@Column
	private String description;//用途
	@Column
	private String defaultValue;//默认值
	
	

}
