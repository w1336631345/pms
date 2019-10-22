package com.kry.pms.model.persistence.busi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.marketing.RoomPriceSchemeItem;
import com.kry.pms.model.persistence.room.RoomType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_booking_item")
public class BookingItem extends PersistenceModel {
	@OneToOne
	private RoomType roomType;
	@Column
	private Integer roomCount;
	@OneToOne
	private RoomPriceSchemeItem PriceSchemeItem;
	@Column
	private Double purchasePrice;
	@Column
	private Integer checkInCount = 0;
	
}
