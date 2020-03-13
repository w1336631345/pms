package com.kry.pms.dao.busi;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.http.response.busi.AccountSummaryVo;
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

	@Query(value = "select new com.kry.pms.model.http.response.busi.AccountSummaryVo(b.orderNum, a.id, d.name,a.total, c.roomNum," +
			"a.type, a.pay, a.cost, a.creditLimit, a.availableCreditLimit," +
			"b.status, a.code, b.arriveTime, b.leaveTime,c.id) from Account a,CheckInRecord b,GuestRoom c,Customer d" +
			" where b.account=a and b.guestRoom = c and b.customer = d and b.orderNum = ?1 and b.deleted=?3 and b.type=?2")
	List<AccountSummaryVo> querySummeryByOrderNumAndTypeAndDeleted(String orderNum,String type, int deletedFalse);

	@Query(nativeQuery = true, value = " select \n" +
			" tcr.id, \n" +
			" tcr.name_ name, \n" +
			" tc.`name` custName, \n" +
			" tcr.group_name groupName, \n" +
			" ta.`code` accountCode, \n" +
			" ta.total, \n" +
			" ta.credit_limit creditLimit, \n" +
			" ta.cost, \n" +
			" ta.pay, \n" +
			" tcr.status, \n" +
			" tcr.order_num orderNum, \n" +
			" tcr.order_type orderType, \n" +
			" tcr.human_count humanCount, \n" +
			" tcr.room_count roomCount, \n" +
			" tcr.chrildren_count chrildrenCount, \n" +
			" DATE_FORMAT(tcr.arrive_time,'%Y-%m-%d %T') arriveTime, \n" +
			" DATE_FORMAT(tcr.leave_time,'%Y-%m-%d %T') leaveTime, \n" +
			" tcr.days, \n" +
			" tcr.hold_time holdTime,\n" +
			" tcr.type_ type, \n" +
			" tcr.contact_name contactName, \n" +
			" tcr.contact_mobile contactMobile, \n" +
			" tcr.protocol_corpation_id, \n" +
			" tpc.`name` corpName, \n" +
			" tcr.remark, \n" +
			" tcr.group_type groupType, \n" +
			" tcr.room_link_id roomLinkId, \n" +
			" DATE_FORMAT(tcr.create_date,'%Y-%m-%d %T') createDate, \n" +
			" tsm.`name` setMealName, \n" +
			" trp.`name` roomPriceSchemeName \n" +
			" from t_checkin_record tcr left join t_customer tc on tcr.customer_id = tc.id \n" +
			" left join t_protocol_corpation tpc on tcr.protocol_corpation_id = tpc.id \n" +
			" left join t_set_meal tsm on tcr.set_meal_id = tsm.id \n" +
			" left join t_room_price_scheme trp on tcr.room_price_scheme_id = trp.id \n" +
			" left join t_account ta on tcr.account_id = ta.id \n" +
			" where tcr.deleted = 0 \n" +
			" and if(:hotelCode is not null && :hotelCode != '', tcr.hotel_code=:hotelCode, 1=1 )  \n" +
			" and (if(:tType is not null && :tType != '', tcr.type_=:tType, 1=2 ) OR if(:fitType is not null && :fitType != '', tcr.fit_type=:fitType, 1=2 ))  \n" +
			" and if(:status is not null && :status != '', if(:status = 'A', tcr.status in (:status,'R'), tcr.status=:status ), 1=1 ) \n" +
			" and if(:groupType is not null && :groupType != '',  tcr.group_type=:groupType, 1=1 ) ",
			countQuery = " select \n" +
					" count(tcr.id) \n" +
					" from t_checkin_record tcr left join t_customer tc on tcr.customer_id = tc.id \n" +
					" left join t_protocol_corpation tpc on tcr.protocol_corpation_id = tpc.id \n" +
					" left join t_set_meal tsm on tcr.set_meal_id = tsm.id \n" +
					" left join t_room_price_scheme trp on tcr.room_price_scheme_id = trp.id \n" +
					" left join t_account ta on tcr.account_id = ta.id \n" +
					" where tcr.deleted = 0 \n" +
					" and if(:hotelCode is not null && :hotelCode != '', tcr.hotel_code=:hotelCode, 1=1 )  \n" +
					" and (if(:tType is not null && :tType != '', tcr.type_=:tType, 1=2 ) OR if(:fitType is not null && :fitType != '', tcr.fit_type=:fitType, 1=2 ))  \n" +
					" and if(:status is not null && :status != '', if(:status = 'A', tcr.status in (:status,'R'), tcr.status=:status ), 1=1 ) \n" +
					" and if(:groupType is not null && :groupType != '',  tcr.group_type=:groupType, 1=1 ) ")
	Page<Map<String, Object>> resverList(Pageable page, @Param("hotelCode") String hotelCode, @Param("tType") String type, @Param("fitType") String fitType,
										 @Param("status") String status, @Param("groupType") String groupType);

	@Query(nativeQuery = true, value = " select room_layout from t_room_layout t where t.check_in_record_id = ?1 ")
	List<String> getRoomLayout(String checkInId);

	@Query(nativeQuery = true, value = " select t.room_requirement from t_room_requirement t where t.check_in_record_id = ?1 ")
	List<String> getRoomRequirement(String checkInId);


}
