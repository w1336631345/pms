package com.kry.pms.model.persistence.goods;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "t_product_category")
public class ProductCategory extends PersistenceModel {
	@Column(columnDefinition = "varchar(40) default NUll COMMENT '名称'")
	private String name;
	@Column
	private String description;
	@Column(name = "code_")
	private String code;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '主图'")
	private String mainPicture;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '缩略图'")
	private String thumbnail;

	@Transient
	private List<ProductType> productTypeList;
}
