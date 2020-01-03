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
@Table(name = "t_corporation")
public class Corporation extends PersistenceModel {
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '名称'")
	private String name;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '手机'")
	private String mobile;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '电话'")
	private String tel;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '主页地址'")
	private String homeUrl;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '省'")
	private String province;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '编码'")
	private String code;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '城市'")
	private String city;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '县'")
	private String area;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '详细地址'")
	private String address;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '描述'")
	private String description;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '联系人'")
	private String contactName;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '联系电话'")
	private String contactMobile;
}
