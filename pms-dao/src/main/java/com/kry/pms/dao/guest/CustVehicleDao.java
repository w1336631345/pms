package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.CustVehicle;

import java.util.List;

public interface CustVehicleDao extends BaseDao<CustVehicle>{

	List<CustVehicle> findByHotelCode(String hotelCode);

	List<CustVehicle> findByCustomerId(String customerId);

}
