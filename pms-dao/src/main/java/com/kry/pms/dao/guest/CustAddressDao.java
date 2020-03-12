package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.CustAddress;

import java.util.List;

public interface CustAddressDao extends BaseDao<CustAddress>{

	List<CustAddress> findByHotelCode(String hotelCode);

	List<CustAddress> findByCustomerId(String customerId);

	CustAddress findByCustomerIdAndIsDefult(String customerId, String isDefult);

}
