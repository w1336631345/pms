package com.kry.pms.model.other.wrapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kry.pms.base.Constants;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomType;

public class CheckInRecordWrapper implements UseInfoAble {

	private CheckInRecord checkInRecord;

	public CheckInRecordWrapper(CheckInRecord checkInRecord) {
		this.checkInRecord = checkInRecord;
	}

	@JsonIgnore
	@Override
	public String getSummaryInfo() {
		return null;
	}

	@JsonIgnore
	@Override
	public String getBusinessKey() {
		return this.checkInRecord.getOrderNum();
	}

	@JsonIgnore
	@Override
	public boolean isGroup() {
		return this.checkInRecord.getType().equals(Constants.Type.CHECK_IN_RECORD_GROUP);
	}

	@JsonIgnore
	@Override
	public boolean isOTA() {
		if (this.checkInRecord.getMarketingSources() != null) {
			return this.checkInRecord.getMarketingSources().getName().equals("OTA");
		}
		return false;
	}

	@JsonIgnore
	@Override
	public boolean isFree() {
		return false;
	}

	@JsonIgnore
	@Override
	public boolean isHourRoom() {
		return false;
	}

	@JsonIgnore
	@Override
	public boolean isArrears() {
		if (this.checkInRecord.getAccount() != null) {
			return this.checkInRecord.getAccount().getTotal() < 0;
		}
		return false;
	}

	@Override
	public LocalDateTime getStartTime() {
		return this.checkInRecord.getArriveTime();
	}

	@Override
	public LocalDateTime getEndTime() {
		return this.checkInRecord.getLeaveTime();
	}

	@JsonIgnore
	@Override
	public boolean isTodayLeave() {
		if (this.checkInRecord.getLeaveTime() != null) {
			return this.checkInRecord.getLeaveTime().toLocalDate().isEqual(LocalDate.now());
		}
		return false;
	}

	@JsonIgnore
	@Override
	public boolean isTodayArrive() {
		if (this.checkInRecord.getArriveTime() != null) {
			return this.checkInRecord.getLeaveTime().toLocalDate().isEqual(LocalDate.now());
		}
		return false;
	}

	@JsonIgnore
	@Override
	public RoomType roomType() {
		return this.checkInRecord.getRoomType();
	}

	@JsonIgnore
	@Override
	public GuestRoom guestRoom() {
		return this.checkInRecord.getGuestRoom();
	}

	@Override
	public Integer getRoomCount() {
		
		return checkInRecord.getRoomCount();
	}
}
