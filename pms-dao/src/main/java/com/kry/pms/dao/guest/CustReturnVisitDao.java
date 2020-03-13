package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.CustReturnVisit;

import java.util.List;

public interface CustReturnVisitDao extends BaseDao<CustReturnVisit>{

	List<CustReturnVisit> findByHotelCode(String hotelCode);

	List<CustReturnVisit> findByCustomerId(String customerId);

}
