package com.kry.pms.model.persistence.marketing;

import java.util.List;

import javax.persistence.*;

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
	@ManyToOne
	private DiscountType discountType;
	@Column
	private Double guestRoom;//客房
	@Column
	private Double restaurant;//餐饮
	@Column
	private Double entertainment;//娱乐
	@Column
	private Double others;//其它
	@Column
	private Double entertain;//款待
	@Column
	private String isUsed;//是否启用
}
