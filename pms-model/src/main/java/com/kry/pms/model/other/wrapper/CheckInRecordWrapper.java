package com.kry.pms.model.other.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kry.pms.base.Constants;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CheckInRecordWrapper implements UseInfoAble {

    private CheckInRecord checkInRecord;

    public CheckInRecordWrapper(CheckInRecord checkInRecord) {
        this.checkInRecord = checkInRecord;
    }

    @JsonIgnore
    @Override
    public String getSummaryInfo() {
        return this.checkInRecord.getName();
    }

    @JsonIgnore
    @Override
    public String getBusinessKey() {
        return this.checkInRecord.getOrderNum();
    }

    @JsonIgnore
    @Override
    public boolean isGroup() {
        return this.checkInRecord.getGroupType().equals(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_YES);
    }

    @JsonIgnore
    @Override
    public boolean isOTA() {
        if (this.checkInRecord.getMarketingSources() != null) {
            return "OTA".equals(this.checkInRecord.getMarketingSources().getName());
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
        LocalDate localDate = null;
        if (this.checkInRecord.getActualTimeOfArrive() != null) {
            return this.checkInRecord.getActualTimeOfArrive();
        } else {
            localDate =  this.checkInRecord.getArriveTime().toLocalDate();
        }
        return LocalDateTime.of(localDate,LocalTime.NOON);
    }

    @Override
    public LocalDateTime getEndTime() {
        LocalDate localDate = null;
        if (this.checkInRecord.getActualTimeOfLeave() != null) {
            return this.checkInRecord.getActualTimeOfLeave();
        }else{
            localDate = this.checkInRecord.getLeaveTime().toLocalDate();
        }
        return LocalDateTime.of(localDate, LocalTime.NOON);
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
    public String uniqueId() {
        return this.checkInRecord.getId();
    }

    @Override
    public Integer getRoomCount() {

        return checkInRecord.getRoomCount();
    }

    @Override
    public String getRoomStatus() {
        if (checkInRecord.getStatus() != null) {
            switch (checkInRecord.getStatus()) {
                case Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN:
                    return Constants.Status.ROOM_STATUS_OCCUPY_CLEAN;
                case Constants.Status.CHECKIN_RECORD_STATUS_OUT_UNSETTLED:
                    return Constants.Status.ROOM_STATUS_VACANT_DIRTY;
                case Constants.Status.CHECKIN_RECORD_STATUS_CHECK_OUT:
                    return Constants.Status.ROOM_STATUS_VACANT_DIRTY;
                case Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION:
                    return Constants.Status.ROOM_STATUS_VACANT_CLEAN;
            }
        }
        return null;
    }

    @Override
    public LocalDateTime getActualStartTime() {
        return this.checkInRecord.getActualTimeOfArrive();
    }
}
