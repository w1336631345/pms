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
import com.kry.pms.dao.report.VerifyOperationDao;
import com.kry.pms.model.persistence.report.VerifyOperation;
import com.kry.pms.service.report.VerifyOperationService;

import javax.annotation.Resource;

@Service
public class  VerifyOperationServiceImpl implements  VerifyOperationService{
	@Resource
	 VerifyOperationDao verifyOperationDao;
	 
	 @Override
	public VerifyOperation add(VerifyOperation verifyOperation) {
		return verifyOperationDao.saveAndFlush(verifyOperation);
	}

	@Override
	public void delete(String id) {
		VerifyOperation verifyOperation = verifyOperationDao.findById(id).get();
		if (verifyOperation != null) {
			verifyOperation.setDeleted(Constants.DELETED_TRUE);
		}
		modify(verifyOperation);
	}

	@Override
	public VerifyOperation modify(VerifyOperation verifyOperation) {
		return verifyOperationDao.saveAndFlush(verifyOperation);
	}

	@Override
	public VerifyOperation findById(String id) {
		return verifyOperationDao.getOne(id);
	}

	@Override
	public List<VerifyOperation> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return verifyOperationDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<VerifyOperation> listPage(PageRequest<VerifyOperation> prq) {
		Example<VerifyOperation> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(verifyOperationDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
