package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustProposalDao;
import com.kry.pms.dao.guest.CustReturnVisitDao;
import com.kry.pms.model.persistence.guest.CustProposal;
import com.kry.pms.model.persistence.guest.CustReturnVisit;
import com.kry.pms.service.guest.CustProposalService;
import com.kry.pms.service.guest.CustReturnVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustReturnVisitServiceImpl implements CustReturnVisitService {
	@Autowired
	CustReturnVisitDao custReturnVisitDao;


	@Override
	public CustReturnVisit add(CustReturnVisit entity) {
		return custReturnVisitDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		custReturnVisitDao.deleteById(id);
	}

	@Override
	public CustReturnVisit modify(CustReturnVisit custReturnVisit) {
		return custReturnVisitDao.saveAndFlush(custReturnVisit);
	}

	@Override
	public CustReturnVisit findById(String id) {
		return custReturnVisitDao.getOne(id);
	}

	@Override
	public List<CustReturnVisit> getAllByHotelCode(String code) {
		return custReturnVisitDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CustReturnVisit> listPage(PageRequest<CustReturnVisit> prq) {
		Example<CustReturnVisit> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(custReturnVisitDao.findAll(ex, req));
	}

	@Override
	public List<CustReturnVisit> getByCustomerId(String customerId) {
		return custReturnVisitDao.findByCustomerId(customerId);
	}
}
