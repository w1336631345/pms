package com.kry.pms.dao.room;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.room.Floor;

public interface FloorDao extends BaseDao<Floor>{

	List<Floor> findByHotelCodeAndStatus(String hotelCode,String status);

}
