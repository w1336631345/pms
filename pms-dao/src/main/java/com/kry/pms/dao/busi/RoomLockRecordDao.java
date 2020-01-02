package com.kry.pms.dao.busi;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.RoomLockRecord;

public interface RoomLockRecordDao extends BaseDao<RoomLockRecord> {

	RoomLockRecord findByGuestRoomIdAndStatus(String id, String normal);

}
