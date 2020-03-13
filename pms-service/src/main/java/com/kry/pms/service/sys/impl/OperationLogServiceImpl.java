package com.kry.pms.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.OperationLogDao;
import com.kry.pms.model.persistence.sys.OperationLog;
import com.kry.pms.service.sys.OperationLogService;

@Service
public class  OperationLogServiceImpl implements  OperationLogService{
	@Autowired
	 OperationLogDao operationLogDao;
	 
	 @Override
	public OperationLog add(OperationLog operationLog) {
		return operationLogDao.saveAndFlush(operationLog);
	}

	@Override
	public void delete(String id) {
		OperationLog operationLog = operationLogDao.findById(id).get();
		if (operationLog != null) {
		}
		modify(operationLog);
	}

	@Override
	public OperationLog modify(OperationLog operationLog) {
		return operationLogDao.saveAndFlush(operationLog);
	}

	@Override
	public OperationLog findById(String id) {
		return operationLogDao.getOne(id);
	}

	@Override
	public List<OperationLog> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return operationLogDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<OperationLog> listPage(PageRequest<OperationLog> prq) {
		Example<OperationLog> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(operationLogDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
