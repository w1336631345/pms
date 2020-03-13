package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.CustPassenger;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CustPassengerDao extends BaseDao<CustPassenger>{

	List<CustPassenger> findByCustomerId(String customerId);

	@Query(nativeQuery = true, value = " select \n" +
			" tc.id, tc.`name`, tc.hotel_code hotelCode, tc.num_code numCode, \n" +
			" tc.certificate_type certificateType, tc.id_card_num idCardNum, \n" +
			" tc.mobile, tc.email, tc.is_passenger isPassemger, tc.is_auto isAuto, \n" +
			" tc.english_frist englishFrist, tc.english_last englishLast, \n" +
			" tc.passenger_type passengerType, tc.autograph, tc.photo \n" +
			" from t_cust_passenger tcp, t_customer tc \n" +
			" where tcp.passenger_id = tc.id \n" +
			" and tcp.customer_id = ?1 ")
	List<Map<String, Object>> getPassengerList(String customerId);

	List<CustPassenger> findByCustomerIdAndPassengerId(String customerId, String passengerId);

}
