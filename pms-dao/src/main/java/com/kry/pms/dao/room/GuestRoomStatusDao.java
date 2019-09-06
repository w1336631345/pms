package com.kry.pms.dao.room;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.room.GuestRoomStatus;

public interface GuestRoomStatusDao extends BaseDao<GuestRoomStatus>{

	GuestRoomStatus findByGuestRoomId(String id);

	List<GuestRoomStatus> findByHotelCodeAndRoomStatus(String hotelCode, String roomStatus);
	@Modifying
	@Query(value="update t_guest_room_status set room_status=?3 where hotel_code = ?1 and room_status= ?2",nativeQuery = true)
	int batchChangeRoomStatus(String hotelCode, String currentRoomStatus, String toRoomStatus);

}
