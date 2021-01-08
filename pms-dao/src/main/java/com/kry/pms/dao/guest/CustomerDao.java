package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CustomerDao extends BaseDao<Customer>{
	
	Customer findByHotelCodeAndIdCardNum(String hotelCode,String idCardNum);

	@Query(nativeQuery = true, value = " select t.room_price_schemes_id " +
			" from t_customer_room_price_schemes t " +
			" where t.customer_id = ?1 ")
	List<String> getByCustId(String custId);

	@Modifying
	@Query(value = "insert into t_customer_room_price_schemes(customer_id,room_price_schemes_id) values(?1,?2)",nativeQuery = true)
	int addRoomPriceSchm(String custId, String roomPriceSchId);

	@Query(nativeQuery = true, value = " select DISTINCT tc.* \n" +
			" from t_customer tc left join t_checkin_record tcr on tc.id = tcr.customer_id \n" +
			" left join t_guest_room tgr on tcr.guest_room_id = tgr.id \n" +
			" left join t_account ta on tc.id = ta.customer_id  \n" +
			" where if(:name is not null && :name != '', (tc.name like CONCAT('%',:name,'%') or ta.`code` like CONCAT('%',:name,'%') or tgr.room_num like CONCAT('%',:name,'%')), 1=1 ) ",
			countQuery = " select DISTINCT count(tc.id) \n" +
					" from t_customer tc left join t_checkin_record tcr on tc.id = tcr.customer_id \n" +
					" left join t_guest_room tgr on tcr.guest_room_id = tgr.id \n" +
					" left join t_account ta on tc.id = ta.customer_id  \n" +
					" where if(:name is not null && :name != '', (tc.name like CONCAT('%',:name,'%') or ta.`code` like CONCAT('%',:name,'%') or tgr.room_num like CONCAT('%',:name,'%')), 1=1 ) ")
	Page<Customer> queryLike(Pageable page, @Param("name") String name);

	@Query(nativeQuery = true, value = " select \n" +
			" tcr.hotel_code hotelCode, tcr.arrive_time arriveTime, tcr.leave_time leaveTime, \n" +
			" tcr.days, trt.`name` roomType, tgr.room_num roomNum, tcr.personal_price personalPrice, \n" +
			" tc.`name` customerName, tcr.room_count roomCount, tcr.human_count humanCount, ta.cost, ta.total, tcr.`status`, \n" +
			" ta.`code` accountNum, tcr.order_num orderNum, tcr.remark, tcr.create_user createUser, \n" +
			" tcr.create_date createDate, tcr.customer_id customerId, \n" +
			" if(tc.customer_type = 'B', tc.`name`, null) corpName \n" +
			" from t_checkin_record tcr left join t_customer tc on tcr.customer_id = tc.id \n" +
			" left join t_account ta on tcr.account_id = ta.id \n" +
			"  left join t_room_type trt on tcr.room_type_id = trt.id \n" +
			"   left join t_guest_room tgr on tcr.guest_room_id = tgr.id \n" +
			" where tcr.customer_id = ?1 ")
	List<Map<String, Object>> getResverInfo(String customerId);

	List<Customer> findByHotelCodeAndNameAndCustomerType(String hotelCode, String name, String customerType);

	@Query(nativeQuery = true, value = " select \n" +
			" tc.id, tc.`name`, tc.num_code numCode, tcm.agreement_type agreementType, \n" +
			" tc.deleted, tc.effective_date effectiveDate, ta.total, \n" +
			" ta.credit_limit creditLimit, ta.`code` arCode, ta.id accountId \n" +
			" from t_customer tc left join t_cust_market tcm on tc.id = tcm.customer_id \n" +
			" left join (select ta.id, ta.total, ta.`code`, ta.customer_id, ta.credit_limit \n" +
			"  from t_account ta where ta.type_='AR' limit 1) ta on tc.id = ta.customer_id \n" +
			" where 1=1 \n" +
			" and if(:hotelCode is not null && :hotelCode != '', tc.hotel_code=:hotelCode, 1=1 )  \n" +
			" and if(:customerType is not null && :customerType != '', tc.customer_type=:customerType, 1=1 )  \n" +
			" and if(:name is not null && :name != '', tc.`name` like CONCAT('%',:name,'%'), 1=1 ) \n" +
			" and if(:numCode is not null && :numCode != '', tc.num_code=:numCode, 1=1 ) ")
	List<Map<String, Object>> getTypeIsB(@Param("hotelCode") String hotelCode, @Param("customerType")String customerType,
										 @Param("name") String name, @Param("numCode") String numCode);

	@Query(nativeQuery = true, value = " select \n" +
			" tc.id, tc.`name`, tc.num_code numCode, tcm.agreement_type agreementType, \n" +
			" tc.deleted, tc.effective_date effectiveDate, ta.total, tc.sales_men_id salesMenId, \n" +
			" ta.credit_limit creditLimit, ta.`code` arCode, ta.id accountId \n" +
			" from t_customer tc left join t_cust_market tcm on tc.id = tcm.customer_id \n" +
			" left join (select ta.id, ta.total, ta.`code`, ta.customer_id, ta.credit_limit \n" +
			"  from t_account ta where ta.type_='AR' limit 1) ta on tc.id = ta.customer_id \n" +
			" where 1=1 and tc.num_code is not null and tc.num_code != '' \n" +
			" and tc.customer_type in ('B','C','D','E','F','G')  \n" +
			" and if(:hotelCode is not null && :hotelCode != '', tc.hotel_code=:hotelCode, 1=1 )  \n" +
			" and if(:name is not null && :name != '', tc.`name` like CONCAT('%',:name,'%'), 1=1 ) \n" +
			" and if(:numCode is not null && :numCode != '', tc.num_code=:numCode, 1=1 ) ")
	List<Map<String, Object>> getTypeCorp(@Param("hotelCode") String hotelCode, @Param("name") String name, @Param("numCode") String numCode);

	@Modifying
	@Query(value = " update t_customer set is_used = ?1 where id = ?2 ",nativeQuery = true)
	int updateIsUsed(String isUsed, String id);
	@Modifying
	@Query(value = " update t_customer set deleted = ?1 where id = ?2 ",nativeQuery = true)
	int updateDeleted(String deleted, String id);

	@Query(nativeQuery = true, value = " select IFNULL(count(id),0) from t_customer tc \n" +
			" where tc.hotel_code = ?1 and DATE_FORMAT(tc.create_date,'%Y-%m-%d') = ?2 ")
	Integer toDayCount(String hotelCode, String createDate);

	@Query(nativeQuery = true, value = " select tc.* from t_customer tc \n" +
			"  where tc.hotel_code = ?1 and tc.deleted = ?2 \n" +
			"   and DATE_FORMAT(tc.birthday,'%m-%d') = ?3 ")
	List<Customer> getBirthdayCust(String hotelCode, int deleted, String birthday);

}
