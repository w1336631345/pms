package com.kry.pms.model.persistence.dict;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_area")
public class Area {
	@Id
	@Column
	private String code;
	@Column
	private String name;
	@Column
	private String parentCode;

}
