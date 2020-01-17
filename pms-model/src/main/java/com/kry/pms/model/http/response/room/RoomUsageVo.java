package com.kry.pms.model.http.response.room;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.kry.pms.model.http.response.busi.CheckInRecordVo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.room.RoomUsage;

import lombok.Data;

@Data
public class RoomUsageVo implements Serializable {
	protected String id;
	private GuestRoomVo guestRoomVo;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String usageStatus;
	private String businesskey;
	private String businessInfo;
	private Long duration;
	private List<CheckInRecordVo> checkInRecordVo;
	private RoomLockRecordVo roomLockRecordVo;
	private RoomRepairRecordVo roomRepairRecordVo;

	public RoomUsageVo(RoomUsage ru) {

		this.businessInfo = ru.getBusinessInfo();
		this.businesskey = ru.getBusinesskey();
		this.startDateTime = ru.getStartDateTime();
		this.endDateTime = ru.getEndDateTime();
		this.usageStatus = ru.getUsageStatus();
		this.duration = ru.getDuration();
		this.id = ru.getId();
		this.guestRoomVo = new GuestRoomVo(ru.getGuestRoom());
	}
}
