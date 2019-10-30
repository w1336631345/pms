package com.kry.pms.dao.busi;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import org.springframework.data.repository.query.Param;

public interface CheckInRecordDao extends BaseDao<CheckInRecord>{
	@Query(value = "select a.* from t_checkin_record a where a.booking_record_id=?1",nativeQuery = true)
	List<CheckInRecord> fingByBookId(String bookId);

	@Query(nativeQuery = true,
		value = "select trt.`name` roomtype, tcr.room_count, tgr.room_num, tc.`name`, DATE_FORMAT(tcr.arrive_time,'%Y-%m-%d %T') arrive_time,  " +
				" DATE_FORMAT(tcr.leave_time,'%Y-%m-%d %T') leave_time, tcr.hold_time, tg.`name` groupname, tcr.`status`, tcr.id, tcr.hotel_code " +
				" from t_checkin_record tcr LEFT JOIN t_guest_room tgr on tcr.guest_room_id = tgr.id " +
				" left join t_room_type trt on tgr.room_type_id = trt.id " +
				" left join t_customer tc on tcr.customer_id = tc.id " +
				" left join t_group tg on tcr.group_id = tg.id " +
				" where SYSDATE() >= tcr.leave_time " +
				" and if(:status is not null && :status != '', tcr.`status`=:status, 1=1 ) " +
				" and if(:hotelCode is not null && :hotelCode != '', tcr.hotel_code=:hotelCode, 1=1 ) ",
		countQuery = "select count(*) " +
				" from t_checkin_record tcr LEFT JOIN t_guest_room tgr on tcr.guest_room_id = tgr.id " +
				" left join t_room_type trt on tgr.room_type_id = trt.id " +
				" left join t_customer tc on tcr.customer_id = tc.id " +
				" left join t_group tg on tcr.group_id = tg.id " +
				" where SYSDATE() >= tcr.leave_time " +
				" and if(:status is not null && :status != '', tcr.`status`=:status, 1=1 ) " +
				" and if(:hotelCode is not null && :hotelCode != '', tcr.hotel_code=:hotelCode, 1=1 ) ")
	Page<Map<String, Object>> unreturnedGuests(Pageable page, @Param("status")String status, @Param("hotelCode")String hotelCode);

	@Query(value = "select status, count(id) scount from t_checkin_record " +
			" where id not in (select id from t_checkin_record where `status`='I' and leave_time < SYSDATE()) " +
			" and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) "+
			" GROUP BY status ",nativeQuery = true)
	List<Map<String, Object>> getStatistics(@Param("hotelCode")String hotelCode);

	List<CheckInRecord> findByOrderNumAndTypeAndDeleted(String orderNum, String type, int deletedFalse);
}
