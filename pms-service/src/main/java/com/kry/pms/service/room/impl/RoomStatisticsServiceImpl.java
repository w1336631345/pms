package com.kry.pms.service.room.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.model.persistence.sys.SystemConfig;
import com.kry.pms.service.room.RoomStatisticsService;
import com.kry.pms.service.room.RoomTypeQuantityService;
import com.kry.pms.service.sys.SystemConfigService;

@Service
public class RoomStatisticsServiceImpl implements RoomStatisticsService {
	@Autowired
	SystemConfigService systemConfigService;
	@Autowired
	RoomTypeQuantityService roomTypeQuantityService;

	@Override
	public boolean booking(RoomType roomType, LocalDateTime checkInDate, int quantity, int days) {
		LocalTime auditTime = systemConfigService.getCriticalTime(roomType.getHotelCode());
		LocalTime checkInTime = checkInDate.toLocalTime();
		int i = 0;
		if (checkInTime.isBefore(auditTime)) {
			i = -1;
		}
		boolean result = true;
		for (int j = 0; j < days; j++) {
			LocalDate quantityDate = checkInDate.plusDays(i).toLocalDate();
			result = roomTypeQuantityService.bookCheck(roomType, quantityDate, quantity);
			if (!result) {
				break;
			}
			i++;
		}
		return result;
	}

	@Override
	public boolean checkOut(GuestRoom guestRoom, LocalDateTime checkOutDate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean changeRoom(RoomRecord roomRecord, GuestRoom newGuestRoom) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean repairRoom(GuestRoom guestRoom) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean repairRoomComplete(GuestRoom guestRoom) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean lockedRoom(GuestRoom guestRoom, LocalDateTime checkInDate, int days) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unlockRoom(GuestRoom guestRoom) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean dirtyRoom(GuestRoom guestRoom) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cleanedRoom(GuestRoom guestRoom) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkIn(GuestRoom guestRoom, LocalDateTime checkInDate, int days, boolean isGroup) {
		// TODO Auto-generated method stub
		return false;
	}


}
