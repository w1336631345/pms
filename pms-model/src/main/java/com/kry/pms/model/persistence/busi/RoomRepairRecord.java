package com.kry.pms.model.persistence.busi;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.dict.RoomRepairReason;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_room_repair_record")
public class RoomRepairRecord extends PersistenceModel implements UseInfoAble {
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

	@Override
	public RoomType getRoomType() {
		return getGuestRoom().getRoomType();
	}

	@Override
	public String getSummaryInfo() {
		return null;
	}

	@Override
	public String getBusinessKey() {
		return getId();
	}

	@Override
	public boolean isGroup() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOTA() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFree() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHourRoom() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isArrears() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTodayLeave() {
		return false;
	}

	@Override
	public boolean isTodayArrive() {
		return false;
	}
}
