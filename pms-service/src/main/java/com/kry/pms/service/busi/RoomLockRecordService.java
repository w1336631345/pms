package com.kry.pms.service.busi;

import java.time.LocalDateTime;
import java.util.List;

import com.kry.pms.model.http.request.busi.GuestRoomOperation;
import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.BaseService;

public interface RoomLockRecordService extends BaseService<RoomLockRecord>{

	RoomLockRecord openLock(String id, String operationEmployeeId);

	List<RoomLockRecord> findByGuestRoomAndStatus(GuestRoom guestRoom, String status);

    boolean lockRoom(GuestRoom gr, GuestRoomOperation op, String type);
}