package com.kry.pms.model.persistence.org;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.sys.StaticResource;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_hotel")
public class Hotel extends PersistenceModel {
	@OneToOne
	private Corporation corporation;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '名称'")
	private String name;
	@OneToOne
	private HotelExtInfo hotelExtInfo;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '联系人'")
	private String contactName;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '联系电话'")
	private String contactMobile;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '电话'")
	private String tel;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '电子邮箱'")
	private String email;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '传真'")
	private String fax;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '主页'")
	private String homeUrl;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '省'")
	private String province;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '市'")
	private String city;
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '县区'")
	private String area;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '详细地址'")
	private String address;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '描述'")
	private String description;
	@Column(columnDefinition = "double default NUll COMMENT '维度'")
	private Double longitude;
	@Column(columnDefinition = "double default NUll COMMENT '经度'")
	private Double latitude;
	@ManyToMany
	private List<HotelTag> hotelTags;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '主图'")
	private String mainPicture;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '缩略图'")
	private String thumbnail;
}
