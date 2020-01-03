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
	@Column(columnDefinition = "varchar(64) default NULL COMMENT '客房状态码'")
	private String statusCode;
	@Column(columnDefinition = "varchar(64) default NULL COMMENT '客房状态'")
	private String statusName;
	@Column(columnDefinition = "int(6) default NULL COMMENT '数量'")
	private Integer quantity;
}
