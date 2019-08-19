package com.kry.pms.model.persistence.busi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_order")
public class Order extends PersistenceModel {
	@Column
	private String nickname;
	@Column
	private String username;
	@Column
	private String mobile;
	@Column
	private String password;

}
