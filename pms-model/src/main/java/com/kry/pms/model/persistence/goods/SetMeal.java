package com.kry.pms.model.persistence.goods;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_set_meal")
public class SetMeal extends PersistenceModel {
	@Column
	private String name;//
	@Column
	private String description;//描述
	@Column
	private String code;//
	@OneToOne
	private Product product;//
	@OneToOne
	private Goods goods;//对应商品（如：早餐、矿泉水、方便面等）
	@Column
	private Integer quantity;//数量
	@Column
	private Double originalPrice;//原价（单价）
	@Column
	private Double discountPrice;//折扣
	@Column
	private Double total;//总价
	@Column
	private String type;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '主图'")
	private String mainPicture;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '缩略图'")
	private String thumbnail;
}
