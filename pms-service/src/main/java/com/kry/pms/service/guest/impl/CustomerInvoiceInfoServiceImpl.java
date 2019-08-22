package com.kry.pms.service.guest.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustomerInvoiceInfoDao;
import com.kry.pms.model.persistence.guest.CustomerInvoiceInfo;
import com.kry.pms.service.guest.CustomerInvoiceInfoService;

@Service
public class  CustomerInvoiceInfoServiceImpl implements  CustomerInvoiceInfoService{
	@Autowired
	 CustomerInvoiceInfoDao customerInvoiceInfoDao;
	 
	 @Override
	public CustomerInvoiceInfo add(CustomerInvoiceInfo customerInvoiceInfo) {
		return customerInvoiceInfoDao.saveAndFlush(customerInvoiceInfo);
	}

	@Override
	public void delete(String id) {
		CustomerInvoiceInfo customerInvoiceInfo = customerInvoiceInfoDao.findById(id).get();
		if (customerInvoiceInfo != null) {
			customerInvoiceInfo.setDeleted(true);
		}
		customerInvoiceInfoDao.saveAndFlush(customerInvoiceInfo);
	}

	@Override
	public CustomerInvoiceInfo modify(CustomerInvoiceInfo customerInvoiceInfo) {
		return customerInvoiceInfoDao.saveAndFlush(customerInvoiceInfo);
	}

	@Override
	public CustomerInvoiceInfo findById(String id) {
		return customerInvoiceInfoDao.getOne(id);
	}

	@Override
	public List<CustomerInvoiceInfo> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return customerInvoiceInfoDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CustomerInvoiceInfo> listPage(PageRequest<CustomerInvoiceInfo> prq) {
		Example<CustomerInvoiceInfo> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(customerInvoiceInfoDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
