package com.kry.pms.model.persistence.goods;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_product_category")
public class ProductCategory extends PersistenceModel {
	@Column
	private String name;
	@Column
	private String description;
}
