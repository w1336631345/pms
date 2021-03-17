package com.kry.pms.dao.busi;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.http.response.busi.AccountSummaryVo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.room.GuestRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CheckInRecordDao extends BaseDao<CheckInRecord> {
    @Query(value = "select a.* from t_checkin_record a where a.booking_record_id=?1", nativeQuery = true)
    List<CheckInRecord> fingByBookId(String bookId);

    //	@Transactional(propagation= Propagation.NOT_SUPPORTED)
    @Query(nativeQuery = true, value = " select * from t_checkin_record where id = ?1 ")
    CheckInRecord logFindById(String id);

    @Query(nativeQuery = true, value = "select trt.`name` roomtype, tcr.room_count, tgr.room_num, tc.`name`, tc.mobile, DATE_FORMAT(tcr.arrive_time,'%Y-%m-%d %T') arrive_time,  "
            + " DATE_FORMAT(tcr.leave_time,'%Y-%m-%d %T') leave_time, tcr.hold_time, tcr.group_name groupname, tcr.`status`, tcr.id, tcr.hotel_code, "
            + " ta.cost, ta.total, ta.pay, ta.id accountId, ta.`code`, tcr.group_type, tcr.order_num "
            + " from t_checkin_record tcr LEFT JOIN t_guest_room tgr on tcr.guest_room_id = tgr.id "
            + " left join t_room_type trt on tcr.room_type_id = trt.id "
            + " left join t_customer tc on tcr.customer_id = tc.id "
            + " left join t_account ta on tcr.account_id = ta.id "
            + " where tcr.type_ != 'G' "
            + " and if(:mainNum = 'N', :businessDate > tcr.arrive_time, 1=1 ) "
            + " and if(:mainNum = 'A', :businessDate > tcr.leave_time, 1=1 ) "
            + " and if(:status = 'X', tcr.deleted = 1 and DATE_FORMAT(tcr.update_date,'%Y-%m-%d') = :ymd, tcr.deleted = 0 ) "
            + " and if(:status is not null && :status != '', tcr.`status`=:status, 1=1 ) "
            + " and if(:hotelCode is not null && :hotelCode != '', tcr.hotel_code=:hotelCode, 1=1 ) ",
            countQuery = "select count(*) "
                    + " from t_checkin_record tcr LEFT JOIN t_guest_room tgr on tcr.guest_room_id = tgr.id "
                    + " left join t_room_type trt on tcr.room_type_id = trt.id "
                    + " left join t_customer tc on tcr.customer_id = tc.id "
                    + " left join t_group tg on tcr.group_id = tg.id "
                    + " where tcr.type_ != 'G' "
                    + " and if(:mainNum = 'N', :businessDate > tcr.arrive_time, 1=1 ) "
                    + " and if(:mainNum = 'A', :businessDate > tcr.leave_time, 1=1 ) "
                    + " and if(:status = 'X', tcr.deleted = 1 and DATE_FORMAT(tcr.update_date,'%Y-%m-%d') = :ymd, tcr.deleted = 0 ) "
                    + " and if(:status is not null && :status != '', tcr.`status`=:status, 1=1 ) "
                    + " and if(:hotelCode is not null && :hotelCode != '', tcr.hotel_code=:hotelCode, 1=1 ) ")
    Page<Map<String, Object>> unreturnedGuests(Pageable page, @Param("mainNum") String mainNum, @Param("status") String status,
                                               @Param("hotelCode") String hotelCode, @Param("businessDate") LocalDateTime businessDate, @Param("ymd") LocalDate ymd);

    @Query(nativeQuery = true, value = "select trt.`name` roomtype, tcr.room_count, tgr.room_num, tc.`name`, tc.mobile, DATE_FORMAT(tcr.arrive_time,'%Y-%m-%d %T') arrive_time,  "
            + " DATE_FORMAT(tcr.leave_time,'%Y-%m-%d %T') leave_time, tcr.hold_time, tcr.group_name groupname, tcr.`status`, tcr.id, tcr.hotel_code, "
            + " ta.cost, ta.total, ta.pay, ta.id accountId, ta.`code`, tcr.group_type, tcr.order_num "
            + " from t_checkin_record tcr LEFT JOIN t_guest_room tgr on tcr.guest_room_id = tgr.id "
            + " left join t_room_type trt on tcr.room_type_id = trt.id "
            + " left join t_customer tc on tcr.customer_id = tc.id "
            + " left join t_account ta on tcr.account_id = ta.id "
            + " where tcr.type_ != 'G' "
            + " and if(:mainNum = 'N', SYSDATE() > tcr.arrive_time, 1=1 ) "
            + " and if(:mainNum = 'A', SYSDATE() > tcr.leave_time, 1=1 ) "
            + " and if(:status = 'X', tcr.deleted = 1, tcr.deleted = 0 ) "
            + " and if(:status is not null && :status != '', tcr.`status`=:status, 1=1 ) "
            + " and if(:hotelCode is not null && :hotelCode != '', tcr.hotel_code=:hotelCode, 1=1 ) ")
    List<Map<String, Object>> unreturnedGuests(@Param("mainNum") String mainNum,
                                               @Param("status") String status, @Param("hotelCode") String hotelCode);

    @Query(value = "select status, count(id) scount from t_checkin_record "
            + " where type_ != 'G' "
            + " and id not in (select id from t_checkin_record where `status`='I' and leave_time > :businessDate) "
            + " and id not in (select id from t_checkin_record where (`status`='R' and arrive_time > :businessDate and deleted = 0) or (`status`='R' and deleted = 1)) "
            + " and id not in (select id from t_checkin_record where `status`='X' and deleted = 1 and DATE_FORMAT(update_date,'%Y-%m-%d') != :ymd)"
            + " and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) "
            + " GROUP BY status ", nativeQuery = true)
    List<Map<String, Object>> getStatistics(@Param("hotelCode") String hotelCode, @Param("businessDate") LocalDateTime businessDate, @Param("ymd") LocalDate ymd);

    @Query(nativeQuery = true, value = " select ta.`code`, trt.`name` roomtype, tcr.room_count, tgr.room_num, tc.`name`,tcr.human_count, \n" +
            "   DATE_FORMAT(tcr.arrive_time,'%Y-%m-%d') arrive_time, DATE_FORMAT(tcr.leave_time,'%Y-%m-%d') leave_time, \n" +
            "   tcr.group_name, tcr.`status`, tcr.id, tcr.hotel_code, trps.`code` roomPriceSchme, \n" +
            "   trps.`name` roomPriceSchmeName, tsm.id setMealId, tsm.`name` setMeal, tcr.purchase_price purchasePrice, \n" +
            "   trr.cost, ta.id accountId, tcr.group_type, tcr.order_num, tds.`name` discountName, trr.is_account_entry \n" +
            " from t_room_record trr \n" +
            "   left join t_checkin_record tcr on trr.check_in_record_id = tcr.id\n" +
            "   left join t_guest_room tgr on tcr.guest_room_id = tgr.id \n" +
            "   left join t_room_type trt on tgr.room_type_id = trt.id \n" +
            "   left join t_customer tc on tcr.customer_id = tc.id \n" +
            "   left join t_account ta on tcr.account_id = ta.id \n" +
            "   left join t_room_price_scheme trps on tcr.room_price_scheme_id = trps.id \n" +
            "   left join t_set_meal tsm on tcr.set_meal_id = tsm.id \n" +
            "   left join t_discount_scheme tds on trr.discount_scheme_id = tds.id \n" +
            " where 1=1 \n" +
            " and tcr.hotel_code = :hotelCode \n" +
            " and trr.record_date = :businessDate " +
            " and if(:status is not null && :status != '', tcr.`status`=:status, 1=1 ) ",
            countQuery = " select count(trr.id) " +
                    " from t_room_record trr \n" +
                    "   left join t_checkin_record tcr on trr.check_in_record_id = tcr.id\n" +
                    "   left join t_guest_room tgr on tcr.guest_room_id = tgr.id \n" +
                    "   left join t_room_type trt on tgr.room_type_id = trt.id \n" +
                    "   left join t_customer tc on tcr.customer_id = tc.id \n" +
                    "   left join t_account ta on tcr.account_id = ta.id \n" +
                    "   left join t_room_price_scheme trps on tcr.room_price_scheme_id = trps.id \n" +
                    "   left join t_set_meal tsm on tcr.set_meal_id = tsm.id \n" +
                    "   left join t_discount_scheme tds on trr.discount_scheme_id = tds.id \n" +
                    " where 1=1 \n" +
                    " and tcr.hotel_code = :hotelCode \n" +
                    " and trr.record_date = :businessDate " +
                    " and if(:status is not null && :status != '', tcr.`status`=:status, 1=1 ) ")
    Page<Map<String, Object>> accountEntryListMap(Pageable page, @Param("hotelCode") String hotelCode,
                                                  @Param("businessDate") LocalDate businessDate, @Param("status") String status);

    List<CheckInRecord> findByOrderNumAndTypeAndDeleted(String orderNum, String type, int deletedFalse);

    List<CheckInRecord> findByHotelCodeAndOrderNumAndTypeAndDeleted(String hotelCode, String orderNum, String type, int deletedFalse);


    List<CheckInRecord> findByHotelCodeAndOrderNumAndDeleted(String hotelCode, String orderNum, int deletedFalse);

    List<CheckInRecord> findByHotelCodeAndOrderNumAndGuestRoomId(String hotelCode, String orderNum, String id);

    CheckInRecord findTopByAccountIdAndDeleted(String id, int deleted);

    List<CheckInRecord> findByOrderNumAndGuestRoomAndDeleted(String orderNum, GuestRoom guestRoom, int delete);
    List<CheckInRecord> findByOrderNumAndGuestRoomAndDeletedAndStatus(String orderNum, GuestRoom guestRoom, int delete, String status);

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

    @Query(nativeQuery = true, value = " select distinct tgr.room_num \n" +
            " from t_guest_room tgr, t_checkin_record tcr \n" +
            " where tgr.id = tcr.guest_room_id and tcr.room_link_id = ?1 ")
    List<String> findRoomNumByLink(String roomLinkId);

    List<CheckInRecord> findByMainRecordAndDeleted(CheckInRecord mainRecord, int deleted);

    @Query(nativeQuery = true, value = " select  "
            + " tcr.id, tcr.order_num, ta.`code`, DATE_FORMAT(tcr.arrive_time,'%Y-%m-%d %T') arrive_time, DATE_FORMAT(tcr.leave_time, '%Y-%m-%d %T') leave_time, "
            + " tcr.name_, tcr.room_count, tcr.human_count, tcr.`status` "
            + " from t_checkin_record tcr left join t_account ta on tcr.account_id = ta.id "
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
            " where tcr.deleted = ?1 and tcr.order_num = ?2  and tcr.guest_room_id = ?3 ")
    int countTogetherRoom(int deletedFalse, String orderNum, String roomId);

    @Query(value = "select new com.kry.pms.model.http.response.busi.AccountSummaryVo(b.orderNum, a.id,b.id, d.name,a.total, c.roomNum," +
            "a.type, a.pay, a.cost, a.creditLimit, a.availableCreditLimit," +
            "b.status, a.code, b.arriveTime, b.leaveTime,c.id,b.personalPrice,b.days, b.actualTimeOfLeave) from Account a,CheckInRecord b,GuestRoom c,Customer d" +
            " where b.account=a and b.guestRoom = c and b.customer = d and b.orderNum = ?1 and b.deleted=?3 and b.type=?2")
    List<AccountSummaryVo> querySummeryByOrderNumAndTypeAndDeleted(String orderNum, String type, int deletedFalse);

    @Query(value = "select new com.kry.pms.model.http.response.busi.AccountSummaryVo(b.orderNum, a.id, b.id, d.name,a.total, c.roomNum," +
            "a.type, a.pay, a.cost, a.creditLimit, a.availableCreditLimit," +
            "b.status, a.code, b.arriveTime, b.leaveTime,c.id,b.personalPrice,b.days,b.actualTimeOfLeave) from Account a,CheckInRecord b,GuestRoom c,Customer d" +
            " where b.account=a and b.guestRoom = c and b.customer = d and b.hotelCode=?1 and b.orderNum = ?2 and b.type=?3 and b.deleted = 0")
    List<AccountSummaryVo> querySummeryByOrderNumAndType(String hotelCode, String orderNum, String type);

    @Query(value = "select new com.kry.pms.model.http.response.busi.AccountSummaryVo(b.orderNum, a.id, b.id, d.name,a.total, c.roomNum," +
            "a.type, a.pay, a.cost, a.creditLimit, a.availableCreditLimit," +
            "b.status, a.code, b.arriveTime, b.leaveTime,c.id,b.personalPrice,b.days ,b.actualTimeOfLeave) from Account a left join CheckInRecord b on b.account=a" +
            " left join GuestRoom c on b.guestRoom = c " +
            " left join Customer d on b.customer = d " +
            " where b.hotelCode=?1 and b.orderNum = ?2 and b.type=?3 and b.fitType=?4 and b.deleted = 0")
    List<AccountSummaryVo> querySummeryByOrderNumAndType3(String hotelCode, String orderNum, String type, String fitType);

    @Query(nativeQuery = true, value = " select \n" +
            " tcr.id, \n" +
            " ta.id accountId, \n" +
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
            " and if(:status is not null && :status != '', tcr.status=:status, 1=1 ) \n" +
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
                    " and if(:status is not null && :status != '', tcr.status=:status, 1=1 ) \n" +
                    " and if(:groupType is not null && :groupType != '',  tcr.group_type=:groupType, 1=1 ) ")
    Page<Map<String, Object>> resverList(Pageable page, @Param("hotelCode") String hotelCode, @Param("tType") String type, @Param("fitType") String fitType,
                                         @Param("status") String status, @Param("groupType") String groupType);

    @Query(nativeQuery = true, value = " select room_layout from t_room_layout t where t.check_in_record_id = ?1 ")
    List<String> getRoomLayout(String checkInId);

    @Query(nativeQuery = true, value = " select t.room_requirement from t_room_requirement t where t.check_in_record_id = ?1 ")
    List<String> getRoomRequirement(String checkInId);

    //资源统计所有查询-下方
    @Query(nativeQuery = true, value = " select DATE_FORMAT(date.date,'%Y-%m-%d') date, t.room_type_id roomTypeId, t.`code`, t.`name` roomType, \n" +
            " sum(t.human_count) humanCount, sum(t.room_count) roomCount, sum(t.cost) cost \n" +
            " from \n" +
            " (select date from t_date) date left join \n" +
            " (select  trr.record_date,tgr.room_type_id, tgr.room_num, trt.`code`, tcr.room_count, trt.`name`, \n" +
            " sum(tcr.human_count) human_count, sum(IFNULL(trr.cost,tcr.personal_price)) cost  \n" +
            " from t_checkin_record tcr left join t_room_record trr on tcr.id = trr.check_in_record_id \n" +
            " left join t_guest_room tgr on trr.guest_room_id = tgr.id \n" +
            " left join t_room_type trt on tgr.room_type_id = trt.id \n" +
            " where tcr.order_num = :orderNum \n" +
            " group by trr.record_date,tgr.room_type_id, tgr.room_num, trt.`code`, tcr.room_count, trt.`name` \n" +
            " ) t on date.date = t.record_date \n" +
            " where date.date >= :arriveTime and date.date < :leaveTime \n" +
            " group by date.date, t.`name`, t.room_type_id, t.`code`  ")
    List<Map<String, Object>> resourceStatistics(@Param("orderNum") String orderNum, @Param("arriveTime") String arriveTime,
                                                 @Param("leaveTime") String leaveTime);

    @Query(nativeQuery = true, value = " select tcr.room_type_id, trt.`code`, trt.`name`, " +
            " sum(tcr.human_count) humanCount, sum(tcr.room_count) roomCount, sum(tcr.purchase_price) cost \n" +
            "  from t_checkin_record tcr left join t_room_type trt on tcr.room_type_id = trt.id " +
            " where tcr.deleted = 0 and tcr.type_ = 'R' and tcr.order_num = :orderNum \n" +
            "  and DATE_FORMAT(tcr.arrive_time,'%Y-%m-%d') <= :nowTime and DATE_FORMAT(tcr.leave_time,'%Y-%m-%d') >= :nowTime " +
            "  and tcr.room_type_id = :roomTypeId group by tcr.room_type_id ")
    Map<String, Object> resourceStatisticsR(@Param("orderNum") String orderNum, @Param("nowTime") String nowTime, @Param("roomTypeId") String roomTypeId);

    @Query(nativeQuery = true, value = " select DISTINCT tcr.room_type_id from t_checkin_record tcr \n" +
            " where tcr.order_num = :orderNum  and deleted = 0 and tcr.room_type_id is not null ")
    List<String> getAllRoomType(@Param("orderNum") String orderNum);

    @Query(nativeQuery = true, value = " select DATE_FORMAT(date,'%Y-%m-%d') date  from t_date \n" +
            " where date >= :arriveTime and date < :leaveTime  ")
    List<String> getTime(@Param("arriveTime") String arriveTime, @Param("leaveTime") String leaveTime);

    @Query(nativeQuery = true, value = " select t.record_date, t.room_type_id roomTypeId, t.`code`, t.`name` roomType, \n" +
            " sum(t.human_count) humanCount, sum(t.room_count) roomCount, sum(t.cost) cost \n" +
            " from (\n" +
            " select  trr.record_date,tcr.room_type_id, tgr.room_num, trt.`code`, tcr.room_count, trt.`name`, \n" +
            " sum(tcr.human_count) human_count, sum(IFNULL(trr.cost,tcr.personal_price)) cost  \n" +
            " from t_checkin_record tcr left join t_room_record trr on tcr.id = trr.check_in_record_id \n" +
            " left join t_guest_room tgr on trr.guest_room_id = tgr.id \n" +
            " left join t_room_type trt on tgr.room_type_id = trt.id \n" +
            " where tcr.order_num = :orderNum and trr.record_date = :nowTime \n" +
            " and tcr.room_type_id = :roomTypeId\n" +
            " group by trr.record_date,tcr.room_type_id, tgr.room_num, trt.`code`, tcr.room_count, trt.`name` \n" +
            " ) t group by t.record_date, t.`name`, t.room_type_id, t.`code`  ")
    Map<String, Object> countMap(@Param("orderNum") String orderNum, @Param("nowTime") String nowTime, @Param("roomTypeId") String roomTypeId);
    //资源统计所有查询接口-上方

    List<CheckInRecord> findByGuestRoomIdAndDeleted(String guestRoomId, int deleted);

    @Query(nativeQuery = true, value = " select tcr.id  from t_checkin_record tcr \n" +
            " where tcr.main_record_id = ?1 and tcr.type_ = ?2 and tcr.deleted = ?3 ")
    List<String> listIdByType(String mainId, String type, int deleted);

    @Query(nativeQuery = true, value = " select tcr.* from t_checkin_record tcr where id = ?1 ")
    CheckInRecord byId(String id);

    @Query(nativeQuery = true, value = " select \n" +
            " tcr.name_,\n" +
            " ta.`code`,\n" +
            " tgr.room_num,\n" +
            " trt.`name` roomTypeName,\n" +
            " tcr.personal_price,\n" +
            " DATE_FORMAT(tcr.arrive_time,'%Y-%m-%d %T') arrive_time,\n" +
            " DATE_FORMAT(tcr.leave_time,'%Y-%m-%d %T') leave_time,\n" +
            " tcr.days,\n" +
            " tc.`name`,\n" +
            " tc.certificate_type,\n" +
            " tc.id_card_num,\n" +
            " tc.nationality,\n" +
            " tc.gender,\n" +
            " tc.address,\n" +
            " tc.mobile,\n" +
            " tcr.remark,\n" +
            " ta.pay,\n" +
            " te.`name` emp\n" +
            "from t_checkin_record tcr \n" +
            " left join t_customer tc on tcr.customer_id = tc.id\n" +
            " left join t_account ta on tcr.account_id = ta.id\n" +
            " left join t_guest_room tgr on tcr.guest_room_id = tgr.id\n" +
            " left join t_room_type trt on tgr.room_type_id = trt.id\n" +
            " left join t_user tu on tcr.create_user = tu.id\n" +
            " left join t_employee te on tu.id = te.user_id\n" +
            " where tcr.id = :checkInRecordId ")
    Map<String, Object> printing(@Param("checkInRecordId") String checkInRecordId);

    CheckInRecord findByCustomer(Customer customer);

    List<CheckInRecord> findByHotelCodeAndStatus(String hotelCode, String status);

    @Query(nativeQuery = true, value = " select * from t_checkin_record \n" +
            "where hotel_code = ?1 and status = ?2 \n" +
            "AND DATE_FORMAT(actual_time_of_leave,'%Y-%m-%d') = ?3 ")
    List<CheckInRecord> getObybusinssDate(String hotelCode, String status, LocalDate businessDate);

    @Query(nativeQuery = true, value = " select count(id) from t_checkin_record \n" +
            "where order_num = ?1 and hotel_code = ?2 and `status` != 'O' and deleted =0 ")
    int isNotCheckOut(String orderNum, String hotelCode);

    @Query(nativeQuery = true, value = " select \n" +
            " tcr.type_,\n" +
            " tcr.room_count,\n" +
            " tcr.human_count," +
            " tcr.personal_price,\n" +
            " trt.id roomTypeId,\n" +
            " trt.`name` roomTypeName,\n" +
            " tgr.room_num,\n" +
            " tcr.corp_id,\n" +
            " tcr.status,\n" +
            " tc.`name` corp_name\n" +
            " from t_checkin_record tcr \n" +
            " left join t_guest_room tgr on tcr.guest_room_id = tgr.id\n" +
            " left join t_customer tc on tcr.corp_id = tc.id \n" +
            " left join t_room_type trt on tgr.room_type_id = trt.id \n" +
            " where tcr.type_ != 'R' " +
            " and tcr.hotel_code = ?1 \n" +
            " and tcr.order_num = ?2 ")
    List<Map<String, Object>> printDeposit(String hotelCode, String orderNum);

    @Query(nativeQuery = true, value = "select a.* from t_checkin_record a,t_account b where a.account_id = b.id and b.code = ?1 and a.hotel_code = ?2")
    CheckInRecord findByAccountCodeAndHotelCode(String code, String hotelCode);

    @Query(nativeQuery = true, value = " select IFNULL(count(id),0) from t_checkin_record \n" +
            "  where hotel_code = ?1 and `status` = 'I' and deleted =0 and type_ = 'C'  ")
    int nowLiveIn(String hotelCode);

    @Query(nativeQuery = true, value = " select IFNULL(count(id),0) from t_checkin_record \n" +
            "  where hotel_code = ?1 and `status` = 'O' and type_ = 'C' \n" +
            " and DATE_FORMAT(actual_time_of_leave,'%Y-%m-%d') = ?2  ")
    int nowCheckOut(String hotelCode, String leaveTime);

    @Modifying
    @Query(value = " update t_checkin_record set remark = ?1 where id = ?2 ",nativeQuery = true)
    int updateRemark(String remark, String cirId);
}
