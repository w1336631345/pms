package com.kry.pms.service.guest;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.request.busi.GuestInfoBo;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.service.BaseService;

import java.util.List;
import java.util.Map;

public interface CustomerService extends BaseService<Customer>{

    String getNum(String hotelCode);

    Customer salesStrategy(Customer customer);

    PageResponse<Customer> listPageQuery(PageRequest<Customer> prq);

    public Customer createOrGetCustomer(String hotelCode, GuestInfoBo guestInfoBo);

	public Customer createTempCustomer(String hotelCode,String tempName);

	public Customer createOrGetCustomer(String hotelCode,String name, String idCardNum, String mobile);

    List<Map<String, Object>> getResverInfo(String customerId);

    List<Customer> findByHotelCodeAndNameAndCustomerType(String hotelCode, String name, String customerType);

    List<Map<String, Object>> getTypeIsB(String hotelCode, String customerType, String name, String numCode);

    int updateIsUsed(String isUsed, String id);

    int updateDeleted(String deleted, String id);
}