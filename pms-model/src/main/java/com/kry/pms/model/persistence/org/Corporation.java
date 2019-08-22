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
	@Column
	private String name;
	@Column
	private String mobile;
	@Column
	private String tel;
	@Column
	private String homeUrl;
	@Column
	private String province;
	@Column
	private String city;
	@Column
	private String area;
	@Column
	private String address;
	@Column
	private String description;
	@Column
	private String contactName;
	@Column
	private String contactMobile;
}
