package com.kry.pms.service.guest;

import com.kry.pms.model.persistence.guest.CustFeatures;
import com.kry.pms.model.persistence.guest.CustVehicle;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface CustFeaturesService extends BaseService<CustFeatures>{

    List<CustFeatures> getByCustomerId(String customerId);

}