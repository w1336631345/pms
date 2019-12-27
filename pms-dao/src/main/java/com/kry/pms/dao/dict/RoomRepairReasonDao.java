package com.kry.pms.dao.dict;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.dict.RoomRepairReason;

public interface RoomRepairReasonDao extends BaseDao<RoomRepairReason>{

	List<RoomRepairReason> findByHotelCodeAndDeleted(String code, int deletedFalse);

}
