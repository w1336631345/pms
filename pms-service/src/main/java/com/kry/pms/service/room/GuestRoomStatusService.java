package com.kry.pms.service.room;

import java.time.LocalDate;
import java.util.List;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.http.response.room.GuestRoomStatusVo;
import com.kry.pms.model.http.response.room.RoomStatusTableVo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.model.persistence.busi.RoomRepairRecord;
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

	public void deleteByRoomId(String id);

	void linkedRoom(String roomId, boolean status);

	void addTogether(String roomId, CheckInRecord checkInRecord);

	void checkIn(CheckInRecord cir);

	public GuestRoomStatusVo detail(String id);

	public GuestRoomStatusVo detailGuestRoom(String id);

	/**
	 * 
	 * @param id  房间id
	 * @param status 状态
	 * @param quantity 数量
	 * @param force 强制更改，前端直接该房态为false
	 * @return
	 */
	DtoResponse<String> changeRoomStatus(String id, String status, int quantity, boolean force);

	public void updateSummary(GuestRoom gr,String oldVal, String newVal);

	boolean unRepairGuestRoom(String id, RoomRepairRecord record);

	boolean unLockGuestRoom(String id, RoomLockRecord record);

	boolean repairGuestRoom(String id, RoomRepairRecord record);

	boolean lockGuestRoom(String id, RoomLockRecord record);

	void changeStatus(UseInfoAble info);

	public GuestRoomStatusVo detailGuestRoomNum(String num,String hotleCode);


    void clearUseInfo(UseInfoAble info);
}