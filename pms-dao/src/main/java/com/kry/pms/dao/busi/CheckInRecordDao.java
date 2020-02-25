package com.kry.pms.dao.busi;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CheckInRecordDao extends BaseDao<CheckInRecord> {
	@Query(value = "select a.* from t_checkin_record a where a.booking_record_id=?1", nativeQuery = true)
	List<CheckInRecord> fingByBookId(String bookId);

	@Query(nativeQuery = true, value = "select trt.`name` roomtype, tcr.room_count, tgr.room_num, tc.`name`, DATE_FORMAT(tcr.arrive_time,'%Y-%m-%d %T') arrive_time,  "
			+ " DATE_FORMAT(tcr.leave_time,'%Y-%m-%d %T') leave_time, tcr.hold_time, tg.`name` groupname, tcr.`status`, tcr.id, tcr.hotel_code "
			+ " from t_checkin_record tcr LEFT JOIN t_guest_room tgr on tcr.guest_room_id = tgr.id "
			+ " left join t_room_type trt on tgr.room_type_id = trt.id "
			+ " left join t_customer tc on tcr.customer_id = tc.id " + " left join t_group tg on tcr.group_id = tg.id "
			+ " where SYSDATE() >= tcr.leave_time "
			+ " and if(:status is not null && :status != '', tcr.`status`=:status, 1=1 ) "
			+ " and if(:hotelCode is not null && :hotelCode != '', tcr.hotel_code=:hotelCode, 1=1 ) ", countQuery = "select count(*) "
					+ " from t_checkin_record tcr LEFT JOIN t_guest_room tgr on tcr.guest_room_id = tgr.id "
					+ " left join t_room_type trt on tgr.room_type_id = trt.id "
					+ " left join t_customer tc on tcr.customer_id = tc.id "
					+ " left join t_group tg on tcr.group_id = tg.id " + " where SYSDATE() >= tcr.leave_time "
					+ " and if(:status is not null && :status != '', tcr.`status`=:status, 1=1 ) "
					+ " and if(:hotelCode is not null && :hotelCode != '', tcr.hotel_code=:hotelCode, 1=1 ) ")
	Page<Map<String, Object>> unreturnedGuests(Pageable page, @Param("status") String status,
			@Param("hotelCode") String hotelCode);

	@Query(value = "select status, count(id) scount from t_checkin_record "
			+ " where id not in (select id from t_checkin_record where `status`='I' and leave_time > SYSDATE()) "
			+ " and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) "
			+ " GROUP BY status ", nativeQuery = true)
	List<Map<String, Object>> getStatistics(@Param("hotelCode") String hotelCode);

	List<CheckInRecord> findByOrderNumAndTypeAndDeleted(String orderNum, String type, int deletedFalse);

//	@EntityGraph(value = "CheckInRecord.roomPriceScheme", type = EntityGraph.EntityGraphType.FETCH)
	List<CheckInRecord> findByOrderNumAndDeleted(String orderNum, int deletedFalse);

	List<CheckInRecord> findByHotelCodeAndOrderNumAndGuestRoomId(String hotelCode, String orderNum, String id);

	CheckInRecord findByAccountId(String id);

	List<CheckInRecord> findByOrderNumAndGuestRoomAndDeleted(String orderNum, GuestRoom guestRoom, int delete);

	@Query(nativeQuery = true, value = " select IFNULL(count(1),0) from ( "
			+ " select guest_room_id, count(guest_room_id) " + " from t_checkin_record "
			+ " where reserve_id = :reserveId " + " and deleted = :deleted " + " group by guest_room_id ) t ")
	int getReserveIdCount(@Param("reserveId") String reserveId, @Param("deleted") int deleted);

	@Query(nativeQuery = true, value = " select IFNULL(count(1),0) from (  "
			+ " select guest_room_id, count(guest_room_id)  " + " from t_checkin_record  "
			+ " where guest_room_id is not null " + " and main_record_id = :mainRecordId " + " and deleted = :deleted "
			+ " group by guest_room_id ) t ")
	int getMainRecordIdCount(@Param("mainRecordId") String mainRecordId, @Param("deleted") int deleted);

	List<CheckInRecord> findByReserveIdAndDeleted(String reserveId, int deleted);

	List<CheckInRecord> findByRoomLinkId(String roomLinkId);

	List<CheckInRecord> findByMainRecordAndDeleted(CheckInRecord mainRecord, int deleted);

	@Query(nativeQuery = true, value = " select  "
			+ " tcr.id, ta.`code`, DATE_FORMAT(tcr.arrive_time,'%Y-%m-%d %T') arrive_time, DATE_FORMAT(tcr.leave_time, '%Y-%m-%d %T') leave_time, "
			+ " tcr.name_, tcr.room_count, tcr.human_count, tcr.`status` "
			+ " from t_checkin_record tcr left join t_account ta " + " on tcr.account_id = ta.id "
			+ " where tcr.deleted =0 and tcr.group_type='Y' and tcr.type_='G' "
			+ " and if(:hotelCode is not null && :hotelCode != '', tcr.hotel_code=:hotelCode, 1=1 ) "
			+ " and if(:arriveTime is not null && :arriveTime != '', DATE_FORMAT(tcr.arrive_time, '%Y-%m-%d')<=:arriveTime, 1=1 ) "
			+ " and if(:leaveTime is not null && :leaveTime != '', DATE_FORMAT(tcr.leave_time, '%Y-%m-%d')>=:leaveTime, 1=1 ) "
			+ " and if(:name_ is not null && :name_ != '', tcr.name_=:name_, 1=1 ) "
			+ " and if(:code_ is not null && :code_ != '', ta.`code`=:code_, 1=1 ) ")
	List<Map<String, Object>> getGroup(@Param("hotelCode") String hotelCode, @Param("arriveTime") String arriveTime,
			@Param("leaveTime") String leaveTime, @Param("name_") String name_, @Param("code_") String code_);

	List<CheckInRecord> findByGuestRoomAndStatusAndDeleted(GuestRoom guestRoom, String status, int deleted);

	List<CheckInRecord> findByGuestRoomIdAndStatusAndDeleted(String guestRoomId, String status, int deleted);

	List<CheckInRecord> findByGuestRoomIdAndStatusAndDeletedAndStartDate(String id, String status, int deleted,
			LocalDate now);

	@Query(nativeQuery = true, value = "select " +
			" id, " +
			" corporation_code corporationCode," +
			" DATE_FORMAT(tcr.create_date,'%Y-%m-%d %T') createDate," +
			" create_user createUser, " +
			" deleted, " +
			" hotel_code hotelCode," +
			" status," +
			" DATE_FORMAT(tcr.update_date,'%Y-%m-%d %T') updateDate," +
			" update_user updateUser, " +
			" DATE_FORMAT(tcr.arrive_time,'%Y-%m-%d %T') arriveTime, " +
			" check_in_count checkInCount, " +
			" check_in_sn checkInSn," +
			" chrildren_count childCount, " +
			" contact_mobile contactMobile, " +
			" contact_name contactName, " +
			" days, " +
			" discount, " +
			" group_type groupType, " +
			" hold_time holdTime, " +
			" human_count humanCount, " +
			" DATE_FORMAT(tcr.leave_time,'%Y-%m-%d %T') leaveTime, " +
			" link_num linkNum, " +
			" linked linked, " +
			" name_ name, " +
			" order_num orderNum, " +
			" order_type orderType,  " +
			" personal_percentage personalPercentage, " +
			" personal_price personalPrice, " +
			" purchase_price purchasePrice, " +
			" regular_price regularPrice,  " +
			" remark,  " +
			" reserve_id reserveId,  " +
			" room_count roomCount, " +
			" room_link_id roomLinkId,  " +
			" single_room_count singleRoomCount,  " +
			" DATE_FORMAT(tcr.start_date,'%Y-%m-%d') startDate,  " +
			" together_code togetherCode,  " +
			" type_ type,  " +
			" booking_record_id bookingRecordId,  " +
			" account_id accountId,  " +
			" customer_id customerId,  " +
			" discount_scheme_id discountSchemeId,  " +
			" distribution_channel_id distributionChannelId,  " +
			" group_id groupId,  " +
			" guest_room_id guestRoomId,  " +
			" main_record_id mainRecordId,  " +
			" market_employee_id marketEmployeeId,  " +
			" marketing_sources_id marketingSourcesId,  " +
			" operation_employee_id operationEmployeeId,  " +
			" price_scheme_item_id priceSchemeItemId, " +
			" protocol_corpation_id protocolCorpationId,  " +
			" room_price_scheme_id roomPriceSchemeId,  " +
			" room_type_id roomTypeId,  " +
			" adult_count adultCount,  " +
			" child_count chrildrenCount, " +
			" is_secrecy isSecrecy,  " +
			" DATE_FORMAT(tcr.actual_time_of_arrive,'%Y-%m-%d %T') actualTimeOfArrive,  " +
			" DATE_FORMAT(tcr.actual_time_of_leave,'%Y-%m-%d %T') actualTimeOfLeave,  " +
			" set_meal_id setMealId " +
			" from t_checkin_record tcr where order_num = ?1 and deleted = ?2 ")
	List<Map<String, Object>> sqlOrderNumAndDeleted(String orderNum, int deletedFalse);

	@Query(nativeQuery = true, value = " select count(1) " +
			" from t_checkin_record tcr " +
			" where tcr.deleted = ?1 and tcr.hotel_code = ?2  and tcr.guest_room_id = ?3 ")
	int countTogetherRoom(int deletedFalse, String hotelCode, String roomId);

}
