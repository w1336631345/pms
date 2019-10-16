package com.kry.pms.dao.room;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.room.RoomType;

public interface RoomTypeDao extends BaseDao<RoomType>{

	List<RoomType> findByHotelCode(String code);
	List<RoomType> findByHotelCodeAndDeleted(String code,int deleted);

}
