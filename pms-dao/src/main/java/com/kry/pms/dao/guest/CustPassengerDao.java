package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.CustPassenger;

import java.util.List;

public interface CustPassengerDao extends BaseDao<CustPassenger>{

	List<CustPassenger> findByCustomerId(String customerId);


}
