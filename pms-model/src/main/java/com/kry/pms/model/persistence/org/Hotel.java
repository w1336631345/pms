package com.kry.pms.model.persistence.org;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.sun.jndi.url.corbaname.corbanameURLContextFactory;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_hotel")
public class Hotel extends PersistenceModel {
	@OneToOne
	private Corporation corporation;
	@Column
	private String name;
	@Column
	private String contactName;
	@Column
	private String contactMobile;
	@Column
	private String tel;
	@Column
	private String email;
	@Column
	private String fax;
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
}
