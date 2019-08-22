package com.kry.pms.service.busi.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.OrderDao;
import com.kry.pms.model.persistence.busi.Order;
import com.kry.pms.service.busi.OrderService;

@Service
public class  OrderServiceImpl implements  OrderService{
	@Autowired
	 OrderDao orderDao;
	 
	 @Override
	public Order add(Order order) {
		return orderDao.saveAndFlush(order);
	}

	@Override
	public void delete(String id) {
		Order order = orderDao.findById(id).get();
		if (order != null) {
			order.setDeleted(true);
		}
		orderDao.saveAndFlush(order);
	}

	@Override
	public Order modify(Order order) {
		return orderDao.saveAndFlush(order);
	}

	@Override
	public Order findById(String id) {
		return orderDao.getOne(id);
	}

	@Override
	public List<Order> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return orderDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Order> listPage(PageRequest<Order> prq) {
		Example<Order> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(orderDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
