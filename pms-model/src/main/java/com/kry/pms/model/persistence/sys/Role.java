package com.kry.pms.model.persistence.sys;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_role")
public class Role extends PersistenceModel {
	@Column
	private String name;
	@Column(name = "type_")
	private String type;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<Function> functions;
}
