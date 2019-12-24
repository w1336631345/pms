package com.kry.pms.service.room;

import java.time.LocalDateTime;
import java.util.List;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.http.response.room.RoomUsageVo;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomUsage;
import com.kry.pms.service.BaseService;

public interface RoomUsageService extends BaseService<RoomUsage> {

	public RoomUsage use(GuestRoom gr, String status, LocalDateTime startTime, LocalDateTime endTime,
			String businessKey, String businessInfo, DtoResponse<String> response);

	RoomUsage use(GuestRoom gr, String status, LocalDateTime startTime, int days, String businesskey,
			String businessInfo, DtoResponse<String> response);

	List<RoomUsageVo> queryUsableGuestRooms(String roomTypeId, LocalDateTime startTime, LocalDateTime endDateTime);

	public List<RoomUsageVo> queryUsableGuestRoomsByBookItemId(String bookItemId);

	public List<RoomUsageVo> queryUsableGuestRoomsByCheckInRecordId(String bookItemId);
	
	public List<RoomUsageVo> queryUsableGuestRoomsByCheckInRecordId(String bookItemId,String roomTypeId,String roomNum);

}