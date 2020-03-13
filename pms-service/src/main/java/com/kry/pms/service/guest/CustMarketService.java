package com.kry.pms.service.guest;

import com.kry.pms.model.persistence.guest.CustMarket;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface CustMarketService extends BaseService<CustMarket>{

    List<CustMarket> getByCustomerId(String customerId);

}