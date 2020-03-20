package com.kry.pms.dao.sys;

import java.util.Collection;
import java.util.List;

import com.kry.pms.model.persistence.guest.Customer;
import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.Account;

public interface AccountDao extends BaseDao<Account> {

	@Query(value = "select a.*,c.room_num from t_account a,t_checkin_record b,t_guest_room c where a.id=b.account_id and b.guest_room_id = c.id and b.order_num=?1 and b.deleted=0 and a.type_=?2", nativeQuery = true)
	public List<Account> findByOrderNum(String orderNum, String checkInType);

	@Query(value = "select a.*,c.room_num from t_account a,t_checkin_record b,t_guest_room c where a.id=b.account_id and b.guest_room_id = c.id and b.order_num=?1 and b.deleted=0 and a.type_=?2 and a.status=?3", nativeQuery = true)
	public Collection<Account> findAccountByOrderNumAndStatusAndCheckInType(String orderNum, String checkInType,
			String status);

	List<Account> findByHotelCodeAndType(String hotelCode, String type);

	List<Account> findByCustomerAndType(Customer customer, String type);
	@Query(nativeQuery = true,value = "select count(id) from t_account a,t_checkin_record b where a.id = b.account_id and b.order_num = ?1 and b.status=?2")
	int queryAccounCountByOrderNumAndStatus(String orderNum,String status);

}
