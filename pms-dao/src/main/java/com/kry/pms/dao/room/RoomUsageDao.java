package com.kry.pms.dao.room;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.kry.pms.model.http.response.room.GuestRoomStatusVo;
import com.kry.pms.model.http.response.room.RoomUsageListVo;
import com.kry.pms.model.persistence.room.GuestRoom;
import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.room.RoomUsage;

public interface RoomUsageDao extends BaseDao<RoomUsage> {

	@Query(value = "select * from  t_room_usage where guest_room_id=?1 and start_date_time <=?2 and end_date_time>=?3 and usage_status='F' order by start_date_time desc limit 1", nativeQuery = true)
	public RoomUsage queryGuestRoomUsable(String guestRoomId, LocalDateTime startTime, LocalDateTime endDateTime);

	@Query(value = "select a.* from  t_room_usage a,t_guest_room b where a.guest_room_id = b.id and b.room_type_id=?1 and a.start_date_time <=?2 and a.end_date_time>=?3 and a.usage_status='F'", nativeQuery = true)
	public List<RoomUsage> queryUsableGuestRooms(String roomTypeId, LocalDateTime startTime, LocalDateTime endDateTime);

	@Query(value = "select a.* from  t_room_usage a,t_guest_room b where a.guest_room_id = b.id and b.roomNum =?1 and a.end_date_time >=?2 order by a.start_date_time desc limit 2", nativeQuery = true)
	public List<RoomUsage> queryRoomUsable(String roomNum, LocalDateTime startTime);

	@Query(value = "select a.* from  t_room_usage a where a.guest_room_id =?1 and a.start_date_time <=?2 and a.usage_status != 'F'order by a.start_date_time desc limit 1", nativeQuery = true)
	public RoomUsage queryGuestRoomUsable(String id, LocalDateTime startTime);

	public RoomUsage findByGuestRoomIdAndBusinesskey(String id, String businessKey);

	public RoomUsage findByGuestRoomIdAndBusinesskeyAndUsageStatus(String id, String businesskey, String usageStatus);

	int deleteByGuestRoom(GuestRoom gr);
	@Query(value = "select new com.kry.pms.model.http.response.room.RoomUsageListVo(a.id, b.roomNum, a.startDateTime, a.endDateTime," + 
			"d.name, a.usageStatus, a.businesskey, a.businessInfo, a.duration,b.id) " +
			"from RoomUsage a ,GuestRoom b,RoomType d where a.guestRoom = b and b.roomType = d and a.usageStatus='F'"
			+ " and d.id = ?1 and a.startDateTime<=?2 and a.endDateTime>=?3")
	List<RoomUsageListVo> queryUsableRoomTypeGuestRooms(String roomTypeId, LocalDateTime startTime, LocalDateTime endDateTime);

	@Query(nativeQuery = true, value = " SELECT \n" +
			" a.id,\n" +
			" b.room_num roomNum, \n" +
			" DATE_FORMAT(a.start_date_time,'%Y-%m-%d %T') startDateTime,\n" +
			" DATE_FORMAT(a.end_date_time,'%Y-%m-%d %T') endDateTime,\n" +
			" d.`name` roomTypeName,\n" +
			" a.usage_status usageStatus,\n" +
			" a.businesskey,\n" +
			" a.business_info businessInfo,\n" +
			" a.duration,\n" +
			" b.id guestRoomId \n" +
			"FROM \n" +
			" t_room_usage a,\n" +
			" t_guest_room b,\n" +
			" t_room_type d,\n" +
			" t_floor e,\n" +
			" t_building f\n" +
			"WHERE\n" +
			" a.guest_room_id = b.id \n" +
			" AND b.room_type_id = d.id \n" +
			" and b.floor_id = e.id \n" +
			" and e.building_id = f.id \n" +
			" AND a.usage_status = 'F' \n" +
			" and if(?1 is not null && ?1 != '', d.id = ?1, 1=1 ) \n" +
			" and if(?2 is not null && ?2 != '', a.start_date_time <= ?2, 1=1 ) \n" +
			" and if(?3 is not null && ?3 != '', a.end_date_time >= ?3, 1=1 ) \n" +
			" and if(?4 is not null && ?4 != '', e.id = ?4, 1=1 ) \n" +
			" and if(?5 is not null && ?5 != '', f.id = ?5, 1=1 ) \n" +
			" order by b.room_num ")
	List<Map<String, Object>> queryUsableRoomTypeGuestRoomsNew(String roomTypeId, LocalDateTime startTime, LocalDateTime endDateTime,
															   String floorId, String buildingId);
	
}
