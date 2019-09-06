package com.kry.pms.model.persistence.dict;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="t_room_lock_reason")
public class RoomLockReason extends PersistenceModel {
	@Column
	private String name;
	@Column
	private String description;
	@Column(name="code_")
	private String code;
	@Column
	private Integer sortNum;
}
