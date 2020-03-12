package com.kry.pms.service.guest;

import com.kry.pms.model.persistence.guest.CustAddress;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface CustAddressService extends BaseService<CustAddress>{


    List<CustAddress> getByCustomerId(String customerId);
}