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
@Table(name = "t_room_tag")
public class RoomTag extends PersistenceModel {
	@Column
	private String name;
	@Column
	private String decription;
	@Column
	private String nickname;
	@Column
	private String color;
	@Column
	private String type;
}
