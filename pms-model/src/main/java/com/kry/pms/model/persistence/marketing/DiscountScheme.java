package com.kry.pms.model.persistence.marketing;

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
@Table(name = "t_discount_scheme")
public class DiscountScheme extends PersistenceModel{
	@Column
	private String name;
	@Column
	private String code;
	@Column
	private String description;
	@Column
	private String type;
	@Column
	private String mode;
	@Column
	private String value;
	@Column
	private Double ceiling;
	@Column
	private Double floor;
	@OneToMany
	private List<ProductCategory> categories;
}
