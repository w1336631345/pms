package com.kry.pms.service.guest.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustomerInvoiceDao;
import com.kry.pms.model.persistence.guest.CustomerInvoice;
import com.kry.pms.service.guest.CustomerInvoiceService;

@Service
public class  CustomerInvoiceServiceImpl implements  CustomerInvoiceService{
	@Autowired
	 CustomerInvoiceDao customerInvoiceDao;
	 
	 @Override
	public CustomerInvoice add(CustomerInvoice customerInvoice) {
		return customerInvoiceDao.saveAndFlush(customerInvoice);
	}

	@Override
	public void delete(String id) {
		CustomerInvoice customerInvoice = customerInvoiceDao.findById(id).get();
		if (customerInvoice != null) {
			customerInvoice.setDeleted(true);
		}
		customerInvoiceDao.saveAndFlush(customerInvoice);
	}

	@Override
	public CustomerInvoice modify(CustomerInvoice customerInvoice) {
		return customerInvoiceDao.saveAndFlush(customerInvoice);
	}

	@Override
	public CustomerInvoice findById(String id) {
		return customerInvoiceDao.getOne(id);
	}

	@Override
	public List<CustomerInvoice> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return customerInvoiceDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CustomerInvoice> listPage(PageRequest<CustomerInvoice> prq) {
		Example<CustomerInvoice> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(customerInvoiceDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
