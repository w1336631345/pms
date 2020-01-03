package com.kry.pms.model.persistence.goods;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_product")
public class Product extends PersistenceModel {
	@Column
	private String name;
	@Column(name = "price_type", columnDefinition = "varchar(40) COMMENT '定价类型 FREE_PRICE 不定价| FIXED_PRICE 固定定价'")
	private String priceType;
	@Column(name = "type_",columnDefinition = "varchar(40) COMMENT '产品类型GOODS 商品 |SERVICE 服务）'")
	private String type;
	@Column(name = "code_")
	private String code;
	@Column
	private Integer direction;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '主图'")
	private String mainPicture;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '缩略图'")
	private String thumbnail;
	@Column(name = "price")
	private Double price;
	@Column
	private Double taxRate;
	@Column
	private String description;
	@OneToOne
	private ProductCategory category;
}
