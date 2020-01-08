package com.kry.pms.service.room.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.service.room.RoomStatisticsService;
import com.kry.pms.service.room.RoomTypeQuantityService;
import com.kry.pms.service.room.RoomUsageService;
import com.kry.pms.service.room.UseInfoAble;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean booking(UseInfoAble info) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelBooking(UseInfoAble info) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkIn(UseInfoAble info) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancleCheckIn(UseInfoAble info) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkOut(UseInfoAble info) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelCheckOut(UseInfoAble info) {
		// TODO Auto-generated method stub
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
