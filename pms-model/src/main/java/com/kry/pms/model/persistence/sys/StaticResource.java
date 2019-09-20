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
@Table(name = "t_static_resource")
public class StaticResource extends PersistenceModel{
	@Column(name="category_",columnDefinition="varchar(20) COMMENT '分类'")
	private String category;
	@Column(name="type_",columnDefinition="varchar(20) COMMENT '媒体类型(img/video)'")
	private String type;
	@Column(name="value_",columnDefinition="varchar(400) COMMENT '资源地址'")
	private String value;
}
