package com.kry.pms.service.room.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.model.func.UseInfoAble;
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

	@Override
	public boolean reserve(UseInfoAble info) {
		roomTypeQuantityService.useRoomType(info.getRoomType(), info.getStartTime().toLocalDate(),
				info.getEndTime().toLocalDate(), Constants.Status.ROOM_USAGE_BOOK);
		return true;
	}

	@Override
	public boolean cancleReserve(UseInfoAble info) {
		roomTypeQuantityService.unUseRoomType(info.getRoomType(), info.getStartTime().toLocalDate(),
				info.getEndTime().toLocalDate(), Constants.Status.ROOM_USAGE_BOOK);
		return true;
	}

	@Override
	public boolean assignRoom(UseInfoAble info) {
		roomUsageService.use(info.getGuestRoom(), Constants.Status.ROOM_USAGE_BOOK, info.getStartTime(),
				info.getEndTime(), info.getBusinessKey(), info.getSummaryInfo());
		return true;
	}

	@Override
	public boolean cancleAssign(UseInfoAble info) {
		roomUsageService.unUse(info.getGuestRoom(), info.getBusinessKey(), null);
		return true;
	}

	@Override
	public boolean booking(UseInfoAble info) {
		return false;
	}

	@Override
	public boolean cancelBooking(UseInfoAble info) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkIn(UseInfoAble info) {
		roomUsageService.changeUseStatus(info.getGuestRoom(), info.getBusinessKey(), Constants.Status.ROOM_USAGE_CHECK_IN);
		return true;
	}

	@Override
	public boolean cancleCheckIn(UseInfoAble info) {
		roomUsageService.unUse(info.getGuestRoom(), info.getBusinessKey(), null);
		return true;
	}

	@Override
	public boolean checkOut(UseInfoAble info) {
		roomUsageService.changeUseStatus(info.getGuestRoom(), info.getBusinessKey(), Constants.Status.ROOM_USAGE_CHECK_OUT);
		return true;
	}

	@Override
	public boolean cancelCheckOut(UseInfoAble info) {
		roomUsageService.changeUseStatus(info.getGuestRoom(), info.getBusinessKey(), Constants.Status.ROOM_USAGE_CHECK_IN);
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
