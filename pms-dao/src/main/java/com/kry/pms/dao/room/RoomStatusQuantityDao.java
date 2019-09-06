package com.kry.pms.dao.room;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.room.RoomStatusQuantity;

public interface RoomStatusQuantityDao extends BaseDao<RoomStatusQuantity> {

	RoomStatusQuantity findByHotelCodeAndStatusCode(String hotelCode, String oldStatus);
	@Modifying
	@Query(value = "update t_room_status_quantity set quantity = quantity+?3 where hotel_code=?1 and status_code=?2", nativeQuery = true)
	int plusQuantity(String hotelCode, String oldStatus, int i);

}
