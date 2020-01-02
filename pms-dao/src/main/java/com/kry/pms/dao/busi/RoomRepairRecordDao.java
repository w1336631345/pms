package com.kry.pms.dao.busi;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.RoomRepairRecord;

public interface RoomRepairRecordDao extends BaseDao<RoomRepairRecord>{

	RoomRepairRecord findByGuestRoomIdAndStatus(String id, String normal);

}
