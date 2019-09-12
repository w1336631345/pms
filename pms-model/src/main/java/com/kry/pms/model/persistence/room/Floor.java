package com.kry.pms.model.persistence.room;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_floor")
public class Floor extends PersistenceModel {
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '名称'")
	private String name;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '描述'")
	private String description;
	@Column
	private Integer sortNum;

}
