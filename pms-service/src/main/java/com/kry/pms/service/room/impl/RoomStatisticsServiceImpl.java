package com.kry.pms.service.room.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.kry.pms.model.persistence.busi.CheckInRecord;
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
        return roomTypeQuantityService.useRoomType(info, Constants.Status.ROOM_USAGE_BOOK);
    }

    @Override
    public boolean cancleReserve(UseInfoAble info) {
        roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getStartTime(),
                info.getEndTime(), Constants.Status.ROOM_USAGE_RESERVATION,
                Constants.Status.ROOM_USAGE_PREDICTABLE, info.getRoomCount());
        return true;
    }

    @Override
    public boolean assignRoom(UseInfoAble info) {
        if (roomUsageService.assignRoom(info)) {
            guestRoomStatusService.changeStatus(info);
            return true;
        }
        return false;
    }

    @Override
    public boolean cancleAssign(UseInfoAble info) {
        return roomUsageService.cancleAssignRoom(info);
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
            return roomUsageService.checkIn(info);
        }
        return false;
    }

    @Override
    public boolean cancleCheckIn(UseInfoAble info) {
        // 未判断是否为同时离开
        boolean result = roomUsageService.cancleCheckIn(info);
        if(result){
            guestRoomStatusService.changeStatus(info);
        }
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
        CheckInRecord cir = (CheckInRecord) info.getSource();
        if (!info.useType().equals(Constants.Type.CHECK_IN_RECORD_RESERVE)) {//如果不是预留订单 就需要修改房间资源
            String roomUseStatus = null;
            if (cir.getStatus().equals(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION)) {
                roomUseStatus = Constants.Status.ROOM_USAGE_ASSIGN;
            } else {
                roomUseStatus = cir.getStatus();
            }
            roomTypeQuantityService.extendTime(info.roomType(), roomUseStatus, info.getStartTime(), info.getEndTime(), newStartTime, newEndTime, 1);
            //下方计算资源，是设计的取开始时间当天12点，到结束时间当天12点
            //若是开始时间12点之后出现过预订取消，就会占用资源，续住就会出现资源不足
//            if (newStartTime != null) {
//                newStartTime = LocalDateTime.of(newStartTime.toLocalDate(), LocalTime.NOON);
//            }
//            if (newEndTime != null) {
//                newEndTime = LocalDateTime.of(newEndTime.toLocalDate(), LocalTime.NOON);
//            }
            if (newStartTime == null || newStartTime.isEqual(info.getStartTime())) {//如果开始时间没有变
                if (newEndTime == null || (newEndTime != null && newEndTime.isEqual(info.getEndTime()))) {
                    return true;
                } else {
                    return extendTime(info, newEndTime.toLocalDate());
                }
            } else {
                //1、如果是续住，应该算当前时间-离店时间是否有资源
                //2、如果是排房预定状态，应该算到店时间-离店时间是否有资源
                if(cir.getStatus().equals(Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN)){//在住
                    return roomUsageService.extendTime2(info, newEndTime);
                }
                return roomUsageService.extendTime(info, newStartTime, newEndTime);
            }
        } else {
            roomTypeQuantityService.extendTime(info.roomType(), cir.getStatus(), info.getStartTime(), info.getEndTime(), newStartTime, newEndTime, cir.getRoomCount());
            return true;
        }
    }

    @Override
    public boolean updateGuestRoomStatus(UseInfoAble info) {
        if (info.guestRoom() != null && Constants.Status.ROOM_STATUS_OCCUPY_CLEAN.equals(info.getRoomStatus()) && info.getStartTime().isBefore(LocalDateTime.now()) && info.getEndTime().isAfter(LocalDateTime.now())) {
            guestRoomStatusService.updateStatus(info);
        }
        return true;
    }

    @Override
    public boolean changeRoom(UseInfoAble info, GuestRoom newGuestRoom, LocalDateTime changeTime) {
        boolean re = roomUsageService.changeRoom(info, newGuestRoom, changeTime);
        if (!re) {
            return false;
        }
        if (!newGuestRoom.getRoomType().getId().equals(info.roomType().getId())) {
            roomTypeQuantityService.changeRoom(info.roomType(), newGuestRoom.getRoomType(), ((CheckInRecord) info.getSource()).getStatus(), info.getStartTime(), info.getEndTime(), changeTime);
        }
        guestRoomStatusService.changeRoom(info.guestRoom(), newGuestRoom, changeTime);
        return true;
    }

    @Override
    public boolean addTogether(UseInfoAble useInfoAble) {
        return roomUsageService.addTogether(useInfoAble);
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
