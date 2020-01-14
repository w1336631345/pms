package com.kry.pms.service.busi;

import java.time.LocalDateTime;
import java.util.List;

import com.kry.pms.model.persistence.busi.RoomRepairRecord;
import com.kry.pms.model.persistence.dict.RoomRepairReason;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.BaseService;

public interface RoomRepairRecordService extends BaseService<RoomRepairRecord> {

	RoomRepairRecord createRecord(GuestRoom gr, LocalDateTime startTime, LocalDateTime endTime, RoomRepairReason rpr,
			String endToStatus);

	RoomRepairRecord openRepair(String id, String operationEmployeeId);

	List<RoomRepairRecord> findByGuestRoomAndStatus(GuestRoom guestRoom, String normal);

}