package com.kry.pms.model.persistence.marketing;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.room.RoomType;

import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name="t_room_price_scheme")
public class RoomPriceScheme extends PersistenceModel{
	private RoomType roomType;
	private Double price;
	private LocalTime startTime;
	private LocalTime endTime;
	private String description;
}
