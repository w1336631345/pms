package com.kry.pms.service.guest.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustomerDao;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.service.guest.CustomerService;

@Service
public class  CustomerServiceImpl implements  CustomerService{
	@Autowired
	 CustomerDao customerDao;
	 
	 @Override
	public Customer add(Customer customer) {
		return customerDao.saveAndFlush(customer);
	}

	@Override
	public void delete(String id) {
		Customer customer = customerDao.findById(id).get();
		if (customer != null) {
			customer.setDeleted(true);
		}
		customerDao.saveAndFlush(customer);
	}

	@Override
	public Customer modify(Customer customer) {
		return customerDao.saveAndFlush(customer);
	}

	@Override
	public Customer findById(String id) {
		return customerDao.getOne(id);
	}

	@Override
	public List<Customer> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return customerDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Customer> listPage(PageRequest<Customer> prq) {
		Example<Customer> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(customerDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
