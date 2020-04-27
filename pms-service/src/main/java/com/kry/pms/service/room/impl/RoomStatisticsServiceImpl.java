package com.kry.pms.service.room.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.kry.pms.model.persistence.room.GuestRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.service.room.GuestRoomStatusService;
import com.kry.pms.service.room.RoomStatisticsService;
import com.kry.pms.service.room.RoomTypeQuantityService;
import com.kry.pms.service.room.RoomUsageService;
import com.kry.pms.service.sys.SystemConfigService;

@Service
public class RoomStatisticsServiceImpl implements RoomStatisticsService {
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    RoomTypeQuantityService roomTypeQuantityService;
    @Autowired
    RoomUsageService roomUsageService;
    @Autowired
    GuestRoomStatusService guestRoomStatusService;


    @Override
    public boolean reserve(UseInfoAble info) {
        roomTypeQuantityService.useRoomType(info, Constants.Status.ROOM_USAGE_BOOK);
        return true;
    }

    @Override
    public boolean cancleReserve(UseInfoAble info) {
        roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getStartTime().toLocalDate(),
                info.getEndTime().toLocalDate(), Constants.Status.ROOM_USAGE_RESERVATION,
                Constants.Status.ROOM_USAGE_PREDICTABLE, info.getRoomCount());
        return true;
    }

    @Override
    public boolean assignRoom(UseInfoAble info) {
       if(roomUsageService.assignRoom(info)){
           guestRoomStatusService.changeStatus(info);
           return true;
       }
       return false;
    }

    @Override
    public boolean cancleAssign(UseInfoAble info) {
        if(roomUsageService.cancleAssignRoom(info)){
            guestRoomStatusService.changeStatus(info);
            return true;
        }
        return false;
    }

//	@Override
//	public boolean booking(UseInfoAble info) {
//		return false;
//	}
//
//	@Override
//	public boolean cancelBooking(UseInfoAble info) {
//		// TODO Auto-generated method stub
//		return false;
//	}

    @Override
    public boolean checkIn(UseInfoAble info) {
        if (info.guestRoom() != null) {
          return   roomUsageService.checkIn(info);
        }
        return false;
    }

    @Override
    public boolean cancleCheckIn(UseInfoAble info) {
        // 未判断是否为同时离开
        roomUsageService.cancleCheckIn(info);
        guestRoomStatusService.changeStatus(info);
        return true;
    }

    @Override
    public boolean checkOut(UseInfoAble info) {
        return roomUsageService.checkOut(info);
    }

    @Override
    public boolean checkOut(GuestRoom guestRoom) {

        return true;
    }

    @Override
    public boolean extendTime(UseInfoAble info, LocalDate extendDate) {
        return roomUsageService.extendTime(info, extendDate);
    }

    @Override
    public boolean extendTime(UseInfoAble info, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        roomTypeQuantityService.extendTime(info.roomType(), info.getRoomStatus(), info.getStartTime(), info.getEndTime(), newStartTime, newEndTime);
        if (newStartTime != null) {
            newStartTime = LocalDateTime.of(newStartTime.toLocalDate(), LocalTime.NOON);
        }
        if (newEndTime != null) {
            newEndTime = LocalDateTime.of(newEndTime.toLocalDate(), LocalTime.NOON);
        }
        if (newStartTime == null || newStartTime.isEqual(info.getStartTime())) {
            if (newEndTime == null || (newEndTime != null && newEndTime.isEqual(info.getEndTime()))) {
                return true;
            } else {
                return extendTime(info, newEndTime.toLocalDate());
            }
        } else {
            return roomUsageService.extendTime(info, newStartTime, newEndTime);
        }
    }

    @Override
    public boolean updateGuestRoomStatus(UseInfoAble info) {
        if (Constants.Status.ROOM_STATUS_OCCUPY_CLEAN.equals(info.getRoomStatus()) && info.getStartTime().isBefore(LocalDateTime.now()) && info.getEndTime().isAfter(LocalDateTime.now())) {
            guestRoomStatusService.updateStatus(info);
        }
        return true;
    }

    @Override
    public boolean changeRoom(UseInfoAble info, GuestRoom newGuestRoom, LocalDateTime changeTime) {
        roomUsageService.changeRoom(info, newGuestRoom, changeTime);
        if (!newGuestRoom.getRoomType().getId().equals(info.roomType().getId())) {
            roomTypeQuantityService.changeRoom(info.roomType(), newGuestRoom.getRoomType(), info.getRoomStatus(), info.getStartTime(), info.getEndTime(), changeTime);
        }
        guestRoomStatusService.changeRoom(info.guestRoom(),newGuestRoom,changeTime);
        return false;
    }


    @Override
    public boolean cancelCheckOut(UseInfoAble info) {
        return roomUsageService.changeUseStatus(info.guestRoom(), info.getBusinessKey(), Constants.Status.ROOM_USAGE_CHECK_IN);
    }

    @Override
    public boolean lock(UseInfoAble info) {
        return roomUsageService.lock(info);
    }

    @Override
    public boolean cancleLock(UseInfoAble info, LocalDateTime cancleDateTime) {
        return roomUsageService.unLock(info, cancleDateTime);
    }
    

}
