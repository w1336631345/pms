package com.kry.pms.model.persistence.busi;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.goods.ProductCategory;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_group")
public class Group extends PersistenceModel {
	@Column
	private String name;
	@Column
	private Boolean groupPayEnable;
	@OneToMany
	private List<ProductCategory> groupPayment;
	@Column
	private String contactName;
	@Column
	private String contactMobile;

}
