package com.kry.pms.model.persistence.room;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_building")
public class Building extends PersistenceModel {
	@Column
	private String name;
	@Column
	private String description;
	@Column
	private Integer sortNum;
}
