package com.kry.pms.dao.busi;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.RoomRepairRecord;
import com.kry.pms.model.persistence.room.GuestRoom;

public interface RoomRepairRecordDao extends BaseDao<RoomRepairRecord>{

//	RoomRepairRecord findByGuestRoomIdAndStatus(String id, String normal);
	
	@Query(value = "select * from t_room_repair_record where guest_room_id = ?1 and status =?2 order by start_time desc limit 1 ",nativeQuery = true)
	RoomRepairRecord queryTopRecord(String guestId,String status);

	List<RoomRepairRecord> findByGuestRoomAndStatus(GuestRoom guestRoom, String status);

}
