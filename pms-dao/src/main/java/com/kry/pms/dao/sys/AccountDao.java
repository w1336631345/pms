package com.kry.pms.dao.sys;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.Account;

public interface AccountDao extends BaseDao<Account>{
	
	@Query(value = "select a.*,c.room_num from t_account a,t_checkin_record b,t_guest_room c where a.id=b.account_id and b.guest_room_id = c.id and b.order_num=?1 and b.deleted=0 and a.type_=?2",nativeQuery = true)
	public List<Account> findByOrderNum(String orderNum,String checkInType);

}
