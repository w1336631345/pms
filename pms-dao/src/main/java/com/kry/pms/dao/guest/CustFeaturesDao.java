package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.CustFeatures;

import java.util.List;

public interface CustFeaturesDao extends BaseDao<CustFeatures>{

	List<CustFeatures> findByHotelCode(String hotelCode);

	List<CustFeatures> findByCustomerId(String customerId);

}
