package com.kry.pms.model.persistence.busi;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.dict.RoomRepairReason;
import com.kry.pms.model.persistence.room.GuestRoom;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_room_repair_record")
public class RoomRepairRecord extends PersistenceModel{
	@OneToOne
	private GuestRoom guestRoom;
	@OneToOne
	private RoomRepairReason reason;
	@Column
	private LocalDateTime startTime;
	@Column
	private LocalDateTime endTime;
	@Column
	private String remark;
	@Column
	private String endToStatus;
	@Column
	private Boolean autoReOpen;
}
