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
import org.springframework.data.repository.query.Param;

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


	@Query(value = "select new com.kry.pms.model.http.response.room.RoomUsageListVo(a.id, b.roomNum, a.startDateTime, a.endDateTime," +
			"d.name, a.usageStatus, a.businesskey, a.businessInfo, a.duration,b.id) " +
			"from RoomUsage a ,GuestRoom b,RoomType d where a.guestRoom = b and b.roomType = d "
			+ " and d.id = ?1 and a.endDateTime>=?2 and a.startDateTime<=?3")
	List<RoomUsageListVo> queryByRoomType(String roomTypeId, LocalDateTime startTime, LocalDateTime endDateTime);

	@Query(value = " select tru.id, tgr.room_num roomNum, tru.start_date_time startDateTime, tru.end_date_time endDateTime, \n" +
			"  trt.`name`, tru.usage_status usageStatus, tru.businesskey, tru.business_info businessInfo, tru.duration, tgr.id \n" +
			" from t_room_usage tru \n" +
			"  left join t_guest_room tgr on tru.guest_room_id = tgr.id \n" +
			"  left join t_room_type trt on tgr.room_type_id = trt.id \n" +
			" where tgr.hotel_code = :hotelCode \n" +
			" and if(coalesce(:ids, null) is not null, tgr.id in (:ids), 1=1 ) " +
			"  and tru.start_date_time <= :startTime and tru.end_date_time >= :endDateTime ", nativeQuery = true)
	List<RoomUsageListVo> queryByRoomType2(@Param("hotelCode")String hotelCode, @Param("ids")List<String> ids,
										   @Param("startTime")LocalDateTime startTime, @Param("endDateTime")LocalDateTime endDateTime);

	@Query(nativeQuery = true, value = " select \n" +
			"  s.will_arrive, s.will_leave, s.hour_room, s.group_, s.overdued, s.ota, s.vip,\n" +
			"  s.room_status, f.id floorId, f.`name` floorName, tb.id buidId, tb.`name` buildName,\n" +
			"  a.id, b.room_num roomNum, d.`name` roomTypeName, " +
			"  DATE_FORMAT(a.start_date_time,'%Y-%m-%d %T') startDateTime, " +
			"  DATE_FORMAT(a.end_date_time,'%Y-%m-%d %T') endDateTime, \n" +
			"  a.usage_status usageStatus, a.businesskey, a.business_info businessInfo, a.duration, b.id guestRoomId, d.id roomTypeId \n" +
			" from t_room_usage a, t_guest_room b, t_room_type d, \n" +
			"  t_guest_room_status s, t_floor f, t_building tb \n" +
			" where a.guest_room_id = b.id and b.room_type_id = d.id \n" +
			"  and a.guest_room_id = s.guest_room_id and b.floor_id = f.id and f.building_id = tb.id \n" +
			"  and a.hotel_code = :hotelCode and a.start_date_time <= :endDateTime and a.end_date_time >= :startTime " +

			"  and if(:will_arrive is not null && :will_arrive != '', s.will_arrive=:will_arrive, 1=1 ) " +
			"  and if(:will_leave is not null && :will_leave != '', s.will_leave=:will_leave, 1=1 ) " +
			"  and if(:hour_room is not null && :hour_room != '', s.hour_room=:hour_room, 1=1 ) " +
			"  and if(:group_ is not null && :group_ != '', s.group_=:group_, 1=1 ) " +
			"  and if(:overdued is not null && :overdued != '', s.overdued=:overdued, 1=1 ) " +
			"  and if(:ota is not null && :ota != '', s.ota=:ota, 1=1 ) " +
			"  and if(:vip is not null && :vip != '', s.vip=:vip, 1=1 ) " +

			"  and if(:floorId is not null && :floorId != '', f.id=:floorId, 1=1 ) " +
			"  and if(:buidId is not null && :buidId != '', tb.id=:buidId, 1=1 ) " +
			"  and if(:roomTypeId is not null && :roomTypeId != '', d.id=:roomTypeId, 1=1 ) " +
			"  and if(:roomNum is not null && :roomNum != '', b.room_num=:roomNum, 1=1 )  ")
	List<Map<String, Object>> miniRoomStatus(@Param("hotelCode")String hotelCode,@Param("startTime")LocalDateTime startTime, @Param("endDateTime")LocalDateTime endDateTime,
											 @Param("will_arrive")String will_arrive,@Param("will_leave")String will_leave,@Param("hour_room")String hour_room,
											 @Param("group_")String group_,@Param("overdued")String overdued,@Param("ota")String ota,@Param("vip")String vip,
											 @Param("floorId")String floorId,@Param("buidId")String buidId,@Param("roomTypeId")String roomTypeId,@Param("roomNum")String roomNum);
	
}
