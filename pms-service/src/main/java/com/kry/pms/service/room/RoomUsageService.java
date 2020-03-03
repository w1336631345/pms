package com.kry.pms.service.room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cglib.core.Local;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.http.response.room.RoomUsageListVo;
import com.kry.pms.model.http.response.room.RoomUsageVo;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomUsage;
import com.kry.pms.service.BaseService;

public interface RoomUsageService extends BaseService<RoomUsage> {

	public RoomUsage use(GuestRoom gr, String status, LocalDateTime startTime, int days, String businesskey,
			String businessInfo, DtoResponse<String> response);

	List<RoomUsageListVo> queryUsableGuestRooms(String roomTypeId, LocalDateTime startTime, LocalDateTime endDateTime);

	public List<RoomUsageListVo> queryUsableGuestRoomsByBookItemId(String bookItemId);

	public List<RoomUsageListVo> queryUsableGuestRoomsByCheckInRecordId(String bookItemId);

	public List<RoomUsageListVo> queryUsableGuestRoomsByCheckInRecordId(String bookItemId, String roomTypeId,
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
	 * @param businesskey       业务id，checkInRecordId，roomLockRecordId，roomRepairRecordId
	 * @param businessInfo
	 * @param roomTypeOperation
	 * @return
	 */
	public DtoResponse<RoomUsage> use(GuestRoom gr, String status, LocalDateTime startTime, LocalDateTime endTime,
			String businesskey, String businessInfo, boolean roomTypeOperation);

	/**
	 * 改变房间使用状态，
	 * 
	 * @param gr          房间号
	 * @param businessKey 业务id
	 * @param status      目标状态
	 * @return
	 */
	boolean changeUseStatus(GuestRoom gr, String businessKey, String status);

	/**
	 *
	 * @param gr 客房
	 * @param businessKey 可共用资源的编码
	 * @param unique 唯一编码
	 * @param status
	 * @return
	 */
	boolean changeUseStatus(GuestRoom gr, String businessKey,String unique, String status);

	public boolean freeCheck(GuestRoom gr, LocalDateTime startTime, LocalDateTime endDateTime);

	boolean unUse(GuestRoom gr, String businessKey, LocalDateTime endTime, String roomTypeUsage);

    public boolean checkIn(UseInfoAble info);

	public boolean cancleCheckIn(UseInfoAble info);

	public boolean assignRoom(UseInfoAble info);

    public boolean cancleAssignRoom(UseInfoAble info);

    public boolean checkOut(UseInfoAble info);

    public boolean cancleCheckOut(UseInfoAble info);

}