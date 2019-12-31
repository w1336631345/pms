package com.kry.pms.service.room;

import java.time.LocalDate;
import java.util.List;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.http.response.room.RoomStatusTableVo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
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

	GuestRoomStatus initNewGuestRoomStatus(GuestRoom guestRoom);

	public RoomStatusTableVo table(String currentHotleCode);

	public DtoResponse<String> changeRoomStatus(String id, String status,int quantity);

	public void deleteByRoomId(String id);

	void linkedRoom(String roomId, boolean status);

	void addTogether(String roomId, CheckInRecord checkInRecord);

	void checkIn(CheckInRecord cir);
	
	
}