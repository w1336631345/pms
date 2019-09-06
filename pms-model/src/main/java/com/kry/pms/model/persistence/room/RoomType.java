package com.kry.pms.model.persistence.room;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_room_type")
public class RoomType extends PersistenceModel {
	@Column
	private String name;
	@Column
	private String price;
	@Column
	private String code;
	@Column
	private String description;
	@Column
	private String sortCode;
	@Column
	private Integer overReservation;
	@Column
	private Integer roomCount;
	@Column
	private Integer bookingVerifyThreshold;

}
