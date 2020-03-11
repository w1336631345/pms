package com.kry.pms.model.persistence.room;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_floor")
public class Floor extends PersistenceModel {
	@ManyToOne
	private Building building;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '名称'")
	@NotBlank
	private String name;
	@Column(columnDefinition = "int(8) COMMENT '层数'")
	private Integer floorNum;
	@NotBlank
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '简称'")
	private String nickname;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '描述'")
	private String description;
	@Column(columnDefinition = "varchar(400) default NULL COMMENT '全景地址'")
	private String overallView;
	@Column(columnDefinition = "varchar(40) default NULL COMMENT '编码'")
	private String code;
	@Column
	private Integer sortNum;

}
