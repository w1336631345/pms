package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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

}
