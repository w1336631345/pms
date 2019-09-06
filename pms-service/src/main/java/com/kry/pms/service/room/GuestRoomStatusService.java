package com.kry.pms.service.room;

import java.time.LocalDate;
import java.util.List;

import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.GuestRoomStatus;
import com.kry.pms.service.BaseService;

public interface GuestRoomStatusService extends BaseService<GuestRoomStatus> {
	public void checkIn(GuestRoom guestRoom, LocalDate checkInDate, String summary, boolean group, boolean linked,
			boolean hourRoom, boolean free, boolean overdued);

	public GuestRoomStatus findGuestRoomStatusByGuestRoom(GuestRoom gr);

	public void dailyVerify(GuestRoom guestRoom);

	public int batchChangeRoomStatus(String hotelCode,String currentRoomStatus,String toRoomStatus);

	public void checkOut(String roomId);
}