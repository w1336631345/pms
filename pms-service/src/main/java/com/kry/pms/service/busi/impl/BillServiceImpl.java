package com.kry.pms.service.busi.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.BillDao;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.service.busi.BillService;

@Service
public class  BillServiceImpl implements  BillService{
	@Autowired
	 BillDao billDao;
	 
	 @Override
	public Bill add(Bill bill) {
		return billDao.saveAndFlush(bill);
	}

	@Override
	public void delete(String id) {
		Bill bill = billDao.findById(id).get();
		if (bill != null) {
			bill.setDeleted(true);
		}
		billDao.saveAndFlush(bill);
	}

	@Override
	public Bill modify(Bill bill) {
		return billDao.saveAndFlush(bill);
	}

	@Override
	public Bill findById(String id) {
		return billDao.getOne(id);
	}

	@Override
	public List<Bill> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return billDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Bill> listPage(PageRequest<Bill> prq) {
		Example<Bill> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(billDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
