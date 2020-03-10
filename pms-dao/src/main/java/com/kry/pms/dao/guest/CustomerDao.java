package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

}
