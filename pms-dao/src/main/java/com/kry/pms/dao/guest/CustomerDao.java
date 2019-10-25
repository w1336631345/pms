package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.Customer;

public interface CustomerDao extends BaseDao<Customer>{
	
	Customer findByIdCardNum(String idCardNum);

}
