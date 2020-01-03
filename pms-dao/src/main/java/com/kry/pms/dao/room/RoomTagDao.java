package com.kry.pms.dao.room;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.room.RoomTag;

public interface RoomTagDao extends BaseDao<RoomTag>{

	List<RoomTag> findByHotelCodeAndDeleted(String currentHotleCode, int deleted);

}
