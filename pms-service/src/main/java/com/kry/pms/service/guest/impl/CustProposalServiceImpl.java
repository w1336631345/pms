package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustProposalDao;
import com.kry.pms.dao.guest.CustVehicleDao;
import com.kry.pms.model.persistence.guest.CustProposal;
import com.kry.pms.model.persistence.guest.CustVehicle;
import com.kry.pms.service.guest.CustProposalService;
import com.kry.pms.service.guest.CustVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustProposalServiceImpl implements CustProposalService {
	@Autowired
	CustProposalDao custProposalDao;


	@Override
	public CustProposal add(CustProposal entity) {
		return custProposalDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		custProposalDao.deleteById(id);
	}

	@Override
	public CustProposal modify(CustProposal custProposal) {
		return custProposalDao.saveAndFlush(custProposal);
	}

	@Override
	public CustProposal findById(String id) {
		return custProposalDao.getOne(id);
	}

	@Override
	public List<CustProposal> getAllByHotelCode(String code) {
		return custProposalDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CustProposal> listPage(PageRequest<CustProposal> prq) {
		Example<CustProposal> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(custProposalDao.findAll(ex, req));
	}

	@Override
	public List<CustProposal> getByCustomerId(String customerId) {
		return custProposalDao.findByCustomerId(customerId);
	}
}
