package com.kry.pms.service.guest;

import com.kry.pms.model.persistence.guest.CustProposal;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface CustProposalService extends BaseService<CustProposal>{

    List<CustProposal> getByCustomerId(String customerId);

}