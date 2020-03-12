package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.CustProposal;

import java.util.List;

public interface CustProposalDao extends BaseDao<CustProposal>{

	List<CustProposal> findByHotelCode(String hotelCode);

	List<CustProposal> findByCustomerId(String customerId);

}
