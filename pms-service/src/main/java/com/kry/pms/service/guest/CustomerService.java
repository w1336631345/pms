package com.kry.pms.service.guest;

import com.kry.pms.model.http.request.busi.GuestInfoBo;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.service.BaseService;

public interface CustomerService extends BaseService<Customer>{
	
	public Customer createOrGetCustomer(GuestInfoBo guestInfoBo);

}