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
import com.kry.pms.dao.sys.FunctionDao;
import com.kry.pms.model.persistence.sys.Function;
import com.kry.pms.service.sys.FunctionService;

@Service
public class  FunctionServiceImpl implements  FunctionService{
	@Autowired
	 FunctionDao functionDao;
	 
	 @Override
	public Function add(Function function) {
		return functionDao.saveAndFlush(function);
	}

	@Override
	public void delete(String id) {
		Function function = functionDao.findById(id).get();
		if (function != null) {
			function.setDeleted(Constants.DELETED_TRUE);
		}
		functionDao.saveAndFlush(function);
	}

	@Override
	public Function modify(Function function) {
		return functionDao.saveAndFlush(function);
	}

	@Override
	public Function findById(String id) {
		return functionDao.getOne(id);
	}

	@Override
	public List<Function> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return functionDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Function> listPage(PageRequest<Function> prq) {
		Example<Function> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(functionDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
