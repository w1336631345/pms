package com.kry.pms.service.busi;

import java.time.LocalDateTime;

import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.model.persistence.dict.RoomLockReason;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.BaseService;

public interface RoomLockRecordService extends BaseService<RoomLockRecord>{

	RoomLockRecord createRecord(GuestRoom gr, LocalDateTime startTime, LocalDateTime endTime, RoomLockReason reason,
			String endToStatus);

}