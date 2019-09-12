package com.kry.pms.model.persistence.goods;

import java.util.List;

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
@Table(name = "t_set_meal")
public class SetMeal extends PersistenceModel {
	@OneToMany
	private List<SetMealItem> item;
	@Column
	private String name;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '主图'")
	private String mainPicture;
	@Column(columnDefinition = "varchar(400) default NUll COMMENT '缩略图'")
	private String thumbnail;
	@Column
	private String description;
	@Column
	private String type;
}
