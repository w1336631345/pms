package com.kry.pms.model.persistence.marketing;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_room_price_scheme")
public class RoomPriceScheme extends PersistenceModel {
	@Column
	private LocalTime startTime;
	@Column
	private LocalTime endTime;
	@Column
	private String description;
	@Column
	private Boolean isDefault;
}
