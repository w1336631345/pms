package com.kry.pms.dao.busi;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.Bill;

public interface BillDao extends BaseDao<Bill> {
	
	public List<Bill> findByHotelCode(String hotelCode);

}
