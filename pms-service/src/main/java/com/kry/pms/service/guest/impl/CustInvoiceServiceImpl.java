package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustInvoiceDao;
import com.kry.pms.model.persistence.guest.CustInvoice;
import com.kry.pms.service.guest.CustInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustInvoiceServiceImpl implements CustInvoiceService {
	@Autowired
	CustInvoiceDao custInvoiceDao;


	@Override
	public CustInvoice add(CustInvoice entity) {
		return custInvoiceDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		custInvoiceDao.deleteById(id);
	}

	@Override
	public CustInvoice modify(CustInvoice custPassenger) {
		return custInvoiceDao.saveAndFlush(custPassenger);
	}

	@Override
	public CustInvoice findById(String id) {
		return custInvoiceDao.getOne(id);
	}

	@Override
	public List<CustInvoice> getAllByHotelCode(String code) {
		return custInvoiceDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CustInvoice> listPage(PageRequest<CustInvoice> prq) {
		Example<CustInvoice> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(custInvoiceDao.findAll(ex, req));
	}

	@Override
	public List<CustInvoice> getByCustomerId(String customerId) {
		return custInvoiceDao.findByCustomerId(customerId);
	}
}
