package com.kry.pms.service.room;

import java.time.LocalDateTime;
import java.util.List;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.http.response.room.RoomUsageVo;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomUsage;
import com.kry.pms.service.BaseService;

public interface RoomUsageService extends BaseService<RoomUsage> {

	public RoomUsage use(GuestRoom gr, String status, LocalDateTime startTime, int days, String businesskey,
			String businessInfo, DtoResponse<String> response);

	List<RoomUsageVo> queryUsableGuestRooms(String roomTypeId, LocalDateTime startTime, LocalDateTime endDateTime);

	public List<RoomUsageVo> queryUsableGuestRoomsByBookItemId(String bookItemId);

	public List<RoomUsageVo> queryUsableGuestRoomsByCheckInRecordId(String bookItemId);

	public List<RoomUsageVo> queryUsableGuestRoomsByCheckInRecordId(String bookItemId, String roomTypeId,
			String roomNum);

	/**
	 * 释放资源
	 * 
	 * @param gr
	 * @param businessKey 业务id，checkInRecordId，roomLockRecordId，roomRepairRecordId
	 * @param endTime     提前结束的时间
	 * @return
	 */
	boolean unUse(GuestRoom gr, String businessKey, LocalDateTime endTime);

	/**
	 * 使用资源
	 * 
	 * @param gr
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @param businesskey  业务id，checkInRecordId，roomLockRecordId，roomRepairRecordId
	 * @param businessInfo
	 * @return
	 */
	public DtoResponse<RoomUsage> use(GuestRoom gr, String status, LocalDateTime startTime, LocalDateTime endTime,
			String businesskey, String businessInfo);
	
	/**
	 * 使用房间资源，是否改变房类资源
	 * 
	 * @param gr
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @param businesskey  业务id，checkInRecordId，roomLockRecordId，roomRepairRecordId
	 * @param businessInfo
	 * @param roomTypeOperation
	 * @return
	 */
	public DtoResponse<RoomUsage> use(GuestRoom gr, String status, LocalDateTime startTime, LocalDateTime endTime,
			String businesskey, String businessInfo,boolean roomTypeOperation);

	/**
	 * 改变房间使用状态，
	 * 
	 * @param gr 房间号
	 * @param businessKey 业务id
	 * @param status 目标状态
	 * @return
	 */
	boolean changeUseStatus(GuestRoom gr, String businessKey, String status);

	public boolean checkIn(UseInfoAble info);

}