package com.kry.pms.model.persistence.busi;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.base.Constants;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.persistence.PersistenceModel;
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
	@Column
	private String reason;
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
	public RoomType roomType() {
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
		return false;
	}

	@Override
	public boolean isOTA() {
		return false;
	}

	@Override
	public boolean isFree() {
		return false;
	}

	@Override
	public boolean isHourRoom() {
		return false;
	}

	@Override
	public boolean isArrears() {
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

	@Override
	public GuestRoom guestRoom() {
		return getGuestRoom();
	}

	@Override
	public String uniqueId() {
		return getId();
	}

	@Override
	public Integer getRoomCount() {
		return 1;
	}

	@Override
	public String getRoomStatus() {
		return Constants.Status.ROOM_STATUS_OUT_OF_ORDER;
	}

	@Override
	public LocalDateTime getActualStartTime() {
		return null;
	}
}
