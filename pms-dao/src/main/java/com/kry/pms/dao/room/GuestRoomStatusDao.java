package com.kry.pms.dao.room;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.http.response.room.GuestRoomStatusVo;
import com.kry.pms.model.persistence.room.GuestRoomStatus;

public interface GuestRoomStatusDao extends BaseDao<GuestRoomStatus> {

	GuestRoomStatus findByGuestRoomId(String id);

	List<GuestRoomStatus> findByHotelCodeAndRoomStatus(String hotelCode, String roomStatus);

	@Modifying
	@Query(value = "update t_guest_room_status set room_status=?3 where hotel_code = ?1 and room_status= ?2", nativeQuery = true)
	int batchChangeRoomStatus(String hotelCode, String currentRoomStatus, String toRoomStatus);

	@Query(value = "select a.* from  t_guest_room_status a ,t_guest_room b,t_floor c where a.guest_room_id = b.id and b.floor_id = c.id and c.id = ?1 and a.deleted=0", nativeQuery = true)
	List<GuestRoomStatus> queryByFloorId(String floorId);
	
	@Query(value = "select new com.kry.pms.model.http.response.room.GuestRoomStatusVo(d.name,a.id, b.roomNum, a.summary, b.id," + 
			"a.roomStatus, a.locked, a.willLeave, a.willArrive, a.hourRoom, a.free," + 
			"a.personal, a.group, a.linkedRoom, a.repairRoom, a.overdued, a.ota) from GuestRoomStatus a ,GuestRoom b,Floor c,RoomType d where a.guestRoom = b and b.floor = c and b.roomType = d and c.id = ?1 and a.deleted=0")
	List<GuestRoomStatusVo> queryViewByFloorId(String floorId);

	GuestRoomStatus findTopByRoomNumAndHotelCode(String num, String hotleCode);

}
