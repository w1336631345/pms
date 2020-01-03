package com.kry.pms.model.persistence.busi;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.goods.ProductCategory;

import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name="t_accounting_setting")
public class AccountingSetting extends PersistenceModel{
	@OneToMany
	private List<ProductCategory> payment;
	@Column(name="enable_")
	private Boolean enable;
	
}
