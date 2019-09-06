package com.kry.pms.service.guest.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustomerLevelDao;
import com.kry.pms.model.persistence.guest.CustomerLevel;
import com.kry.pms.service.guest.CustomerLevelService;

@Service
public class  CustomerLevelServiceImpl implements  CustomerLevelService{
	@Autowired
	 CustomerLevelDao customerLevelDao;
	 
	 @Override
	public CustomerLevel add(CustomerLevel customerLevel) {
		return customerLevelDao.saveAndFlush(customerLevel);
	}

	@Override
	public void delete(String id) {
		CustomerLevel customerLevel = customerLevelDao.findById(id).get();
		if (customerLevel != null) {
			customerLevel.setDeleted(Constants.DELETED_TRUE);
		}
		customerLevelDao.saveAndFlush(customerLevel);
	}

	@Override
	public CustomerLevel modify(CustomerLevel customerLevel) {
		return customerLevelDao.saveAndFlush(customerLevel);
	}

	@Override
	public CustomerLevel findById(String id) {
		return customerLevelDao.getOne(id);
	}

	@Override
	public List<CustomerLevel> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return customerLevelDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CustomerLevel> listPage(PageRequest<CustomerLevel> prq) {
		Example<CustomerLevel> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(customerLevelDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
