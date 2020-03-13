package com.kry.pms.service.guest;

import com.kry.pms.model.persistence.guest.CustProposal;
import com.kry.pms.model.persistence.guest.CustReturnVisit;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface CustReturnVisitService extends BaseService<CustReturnVisit>{

    List<CustReturnVisit> getByCustomerId(String customerId);

}