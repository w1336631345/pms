package com.kry.pms.service.guest;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.request.busi.GuestInfoBo;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.service.BaseService;

public interface CustomerService extends BaseService<Customer>{

    String getNum(String hotelCode);

    Customer salesStrategy(Customer customer);

    PageResponse<Customer> listPageQuery(PageRequest<Customer> prq);

    public Customer createOrGetCustomer(String hotelCode, GuestInfoBo guestInfoBo);

	public Customer createTempCustomer(String hotelCode,String tempName);

	public Customer createOrGetCustomer(String hotelCode,String name, String idCardNum, String mobile);

}