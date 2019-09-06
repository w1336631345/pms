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
@Table(name = "t_room_status_quantity")
public class RoomStatusQuantity extends PersistenceModel{
	@Column
	private String statusCode;
	@Column
	private String statusName;
	@Column
	private Integer quantity;
}
