package com.kry.pms.service.report.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.report.VerifyStepDao;
import com.kry.pms.model.persistence.report.VerifyStep;
import com.kry.pms.service.report.VerifyStepService;

@Service
public class  VerifyStepServiceImpl implements  VerifyStepService{
	@Autowired
	 VerifyStepDao verifyStepDao;
	 
	 @Override
	public VerifyStep add(VerifyStep verifyStep) {
		return verifyStepDao.saveAndFlush(verifyStep);
	}

	@Override
	public void delete(String id) {
		VerifyStep verifyStep = verifyStepDao.findById(id).get();
		if (verifyStep != null) {
			verifyStep.setDeleted(Constants.DELETED_TRUE);
		}
		modify(verifyStep);
	}

	@Override
	public VerifyStep modify(VerifyStep verifyStep) {
		return verifyStepDao.saveAndFlush(verifyStep);
	}

	@Override
	public VerifyStep findById(String id) {
		return verifyStepDao.getOne(id);
	}

	@Override
	public List<VerifyStep> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return verifyStepDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<VerifyStep> listPage(PageRequest<VerifyStep> prq) {
		Example<VerifyStep> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(verifyStepDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
