package com.kry.pms.dao.busi;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomLockRecordDao extends BaseDao<RoomLockRecord> {

//	RoomLockRecord findByGuestRoomIdAndStatus(String id, String normal);

	@Query(value = "select * from t_room_lock_record where guest_room_id = ?1 and status =?2 order by start_time desc limit 1 ", nativeQuery = true)
	RoomLockRecord queryTopRecord(String guestId, String status);

	List<RoomLockRecord> findByGuestRoomAndStatus(GuestRoom guestRoom, String status);
}
