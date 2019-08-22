package com.kry.pms.model.persistence.marketing;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.room.RoomType;

import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "t_room_price_scheme_item")
public class RoomPriceSchemeItem extends PersistenceModel{
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
}
