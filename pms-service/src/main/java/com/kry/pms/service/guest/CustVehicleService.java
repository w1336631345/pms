package com.kry.pms.service.guest;

import com.kry.pms.model.persistence.guest.CustVehicle;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface CustVehicleService extends BaseService<CustVehicle>{

    List<CustVehicle> getByCustomerId(String customerId);

}