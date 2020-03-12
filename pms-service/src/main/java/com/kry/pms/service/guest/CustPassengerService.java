package com.kry.pms.service.guest;

import com.kry.pms.model.persistence.guest.CustPassenger;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface CustPassengerService extends BaseService<CustPassenger>{


    List<CustPassenger> getByCustomerId(String customerId);
}