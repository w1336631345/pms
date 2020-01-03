package com.kry.pms.dao.room;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.room.GuestRoom;

public interface GuestRoomDao extends BaseDao<GuestRoom> {
	GuestRoom findByHotelCodeAndRoomNumAndDeleted(String hotelCode, String roomNum,int deleted);

}
