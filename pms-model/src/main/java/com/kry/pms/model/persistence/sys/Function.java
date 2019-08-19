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
@Table(name="t_function")
public class Function extends PersistenceModel {
	@Column
	private String name;
	@Column
	private String nickname;
	@Column
	private String requestUrl;
	@Column
	private String webRoutePath;
	@Column(name = "type_")
	private String type;
}
