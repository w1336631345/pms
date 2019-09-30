package com.kry.pms.model.persistence.room;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.sys.StaticResource;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_building")
public class Building extends PersistenceModel {
	@NotBlank
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '名称'")
	private String name;
	@NotBlank
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '简称'")
	private String nickname;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '描述'")
	private String description;
	@Column(columnDefinition = "varchar(400) default NULL COMMENT '全景地址'")
	private String overallView;
	@Column(columnDefinition = "varchar(40) default NULL COMMENT '编码'")
	private String code;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '主图'")
	private String mainPicture;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '缩略图'")
	private String thumbnail;
	@OneToMany(cascade = CascadeType.ALL)
	private List<StaticResource> pictures;
	@Column
	private Integer sortNum;
}
