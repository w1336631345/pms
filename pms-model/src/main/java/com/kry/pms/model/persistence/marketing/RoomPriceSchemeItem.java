package com.kry.pms.model.persistence.marketing;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.room.RoomType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_room_price_scheme_item")
public class RoomPriceSchemeItem extends PersistenceModel {
	@OneToOne
	private RoomType roomType;
	@Column
	private Double price;
	@Column
	private String name;
	@Column
	private String code;
	@Column
	private String description;
	@OneToMany
	@JoinColumn(name="extra_")
	private List<Product> extra;
}