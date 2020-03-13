package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.CustMarket;
import com.kry.pms.model.persistence.guest.CustProposal;

import java.util.List;

public interface CustMarketDao extends BaseDao<CustMarket>{

	List<CustMarket> findByHotelCode(String hotelCode);

	List<CustMarket> findByCustomerId(String customerId);

}
