package com.kry.pms.dao.busi;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRecordDao extends BaseDao<RoomRecord>{

	List<RoomRecord> findByHotelCodeAndRecordDate(String hotelCode, LocalDate recordDate);

	List<RoomRecord> findByHotelCodeAndCheckInRecord(String hotelCode, CheckInRecord checkInRecord);
	List<RoomRecord> findByCheckInRecord(CheckInRecord checkInRecord);

	@Query(nativeQuery = true, value = " select DATE_FORMAT(tr.record_date, '%Y-%m-%d') record_date, tr.room_price \n" +
			" from t_room_record tr where 1=1 " +
			" and if(:recordDate is not null && :recordDate != '', DATE_FORMAT(tr.record_date, '%Y-%m-%d')=:recordDate, 1=1 ) " +
			" and if(:recordId is not null && :recordId != '', tr.check_in_record_id=:recordId, 1=1 ) ")
	Map<String, Object> recordDateAndRoomPrice(@Param("recordDate") String recordDate, @Param("recordId") String recordId);

	@Query(nativeQuery = true, value = " select * from t_room_record \n" +
			" where record_date >= :recordDate and check_in_record_id = :checkInRecordId ")
	List<RoomRecord> recordDateAndCheckInRecord(@Param("recordDate") LocalDate recordDate, @Param("checkInRecordId") String checkInRecordId);

	@Modifying
	@Query(value = " update t_room_record set is_account_entry = ?1 where id = ?2 ",nativeQuery = true)
	int updateIsAccountEntry(String isAccountEntry, String id);

	@Query(nativeQuery = true, value = " select \n" +
			" trr.id, \n" +
			" tcr.id cirId, \n" +
			" tgr.room_num roomNum, \n" +
			" main.account_id mainAccountId, \n" +
			" tsm.id setMealId, \n" +
			" tsm.product_id productId, \n" +
			" tsm.account_id setMealAccountId, \n" +
			" tcr.account_id cirAccountId, \n" +
			" trr.cost, \n" +
			" tcr.hotel_code hotelCode, \n" +
			" tsm.total setMealCost \n" +
			" from t_room_record trr \n" +
			"  left join t_checkin_record tcr on trr.check_in_record_id = tcr.id \n" +
			"  left join t_set_meal tsm on tcr.set_meal_id = tsm.id \n" +
			"  left join t_checkin_record main on tcr.main_record_id = main.id \n" +
			"  left join t_guest_room tgr on trr.guest_room_id = tgr.id \n" +
			" where 1=1 and tcr.`status` = 'I' " +
			"  and trr.record_date = ?1 \n" +
			"  and trr.hotel_code = ?2 \n" +
			"  and trr.is_account_entry = ?3 ")
	List<Map<String, Object>> auditRoomRecord(LocalDate recordDate, String hotelCode, String isAccountEntry);

}
