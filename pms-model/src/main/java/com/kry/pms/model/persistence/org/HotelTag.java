package com.kry.pms.model.persistence.org;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_hotel_tag")
public class HotelTag extends PersistenceModel {
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '分类'")
	private String category;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '名称'")
	private String name;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '描述'")
	private String description;
	@Column(name = "type_",columnDefinition = "varchar(40) default NUll COMMENT '类型'")
	private String type;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '显示名称'")
	private String showName;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT 'icon'")
	private String showIcon;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '关键字段，如：免费'")
	private String keyInfo;

}
