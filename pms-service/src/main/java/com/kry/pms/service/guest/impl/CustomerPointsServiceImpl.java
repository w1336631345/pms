package com.kry.pms.service.guest.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustomerPointsDao;
import com.kry.pms.model.persistence.guest.CustomerPoints;
import com.kry.pms.service.guest.CustomerPointsService;

@Service
public class  CustomerPointsServiceImpl implements  CustomerPointsService{
	@Autowired
	 CustomerPointsDao customerPointsDao;
	 
	 @Override
	public CustomerPoints add(CustomerPoints customerPoints) {
		return customerPointsDao.saveAndFlush(customerPoints);
	}

	@Override
	public void delete(String id) {
		CustomerPoints customerPoints = customerPointsDao.findById(id).get();
		if (customerPoints != null) {
			customerPoints.setDeleted(true);
		}
		customerPointsDao.saveAndFlush(customerPoints);
	}

	@Override
	public CustomerPoints modify(CustomerPoints customerPoints) {
		return customerPointsDao.saveAndFlush(customerPoints);
	}

	@Override
	public CustomerPoints findById(String id) {
		return customerPointsDao.getOne(id);
	}

	@Override
	public List<CustomerPoints> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return customerPointsDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CustomerPoints> listPage(PageRequest<CustomerPoints> prq) {
		Example<CustomerPoints> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(customerPointsDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
