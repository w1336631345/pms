package com.kry.pms.dao.dict;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.dict.RoomLockReason;

public interface RoomLockReasonDao extends BaseDao<RoomLockReason>{

	List<RoomLockReason> findByHotelCode(String code);

	List<RoomLockReason> findByHotelCodeAndDeleted(String code, int deletedFalse);

}
