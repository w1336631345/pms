package com.kry.pms.dao.busi;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.sys.Account;

public interface BillDao extends BaseDao<Bill> {
	
	public List<Bill> findByHotelCode(String hotelCode);

	public List<Bill> findByAccountId(String id);
	
	public List<Bill> findByAccountAndStatus(Account account,String status);

}
