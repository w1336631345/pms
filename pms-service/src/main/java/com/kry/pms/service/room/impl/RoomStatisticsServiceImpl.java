package com.kry.pms.service.room.impl;

import java.time.LocalDateTime;

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
		roomUsageService.use(info.guestRoom(), Constants.Status.ROOM_USAGE_ASSIGN, info.getStartTime(),
				info.getEndTime(), info.getBusinessKey(), info.getSummaryInfo());
		guestRoomStatusService.changeStatus(info);
		return true;
	}

	@Override
	public boolean cancleAssign(UseInfoAble info) {
		roomUsageService.unUse(info.guestRoom(), info.getBusinessKey(), null, Constants.Status.ROOM_USAGE_RESERVATION);
		guestRoomStatusService.changeStatus(info);
		return true;
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
			roomUsageService.checkIn(info);
			guestRoomStatusService.changeStatus(info);
		}
		return true;
	}

	@Override
	public boolean cancleCheckIn(UseInfoAble info) {
		// 未判断是否为同时离开
		roomUsageService.changeUseStatus(info.guestRoom(), info.getBusinessKey(), Constants.Status.ROOM_USAGE_ASSIGN);
		guestRoomStatusService.changeStatus(info);
		return true;
	}

	@Override
	public boolean checkOut(UseInfoAble info) {
		roomUsageService.changeUseStatus(info.guestRoom(), info.getBusinessKey(),
				Constants.Status.ROOM_USAGE_CHECK_OUT);
		return true;
	}

	@Override
	public boolean cancelCheckOut(UseInfoAble info) {
		roomUsageService.changeUseStatus(info.guestRoom(), info.getBusinessKey(), Constants.Status.ROOM_USAGE_CHECK_IN);
		return false;
	}

	@Override
	public boolean repair(UseInfoAble info) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelRepair(UseInfoAble info, LocalDateTime cancleDateTime) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean lock(UseInfoAble info) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancleLock(UseInfoAble info, LocalDateTime cancleDateTime) {
		// TODO Auto-generated method stub
		return false;
	}

}
