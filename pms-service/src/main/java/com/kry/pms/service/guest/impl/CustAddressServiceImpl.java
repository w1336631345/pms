package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustAddressDao;
import com.kry.pms.dao.guest.CustPassengerDao;
import com.kry.pms.model.persistence.guest.CustAddress;
import com.kry.pms.model.persistence.guest.CustPassenger;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.service.guest.CustAddressService;
import com.kry.pms.service.guest.CustPassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CustAddressServiceImpl implements CustAddressService {
	@Autowired
	CustAddressDao custAddressDao;


	@Override
	public CustAddress add(CustAddress entity) {
		return custAddressDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		custAddressDao.deleteById(id);
	}

	@Override
	@Transactional
	public CustAddress modify(CustAddress custAddress) {
		CustAddress ca = custAddressDao.getOne(custAddress.getId());
		if(!custAddress.getIsDefult().equals(ca.getIsDefult())){//如果与之前是否默认不等，表明修改了默认装填
			if(("Y").equals(custAddress.getIsDefult())){//如果设置了默认，其它之前的默认就应该设为非默认
				CustAddress custAddress1 = custAddressDao.findByCustomerIdAndIsDefult(ca.getCustomerId(), "Y");
				//修改之前的默认地址为非默认
				custAddress1.setIsDefult("N");
				custAddressDao.saveAndFlush(custAddress1);
			}
		}
		return custAddressDao.saveAndFlush(custAddress);
	}

	@Override
	public CustAddress findById(String id) {
		return custAddressDao.getOne(id);
	}

	@Override
	public List<CustAddress> getAllByHotelCode(String code) {
		return custAddressDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CustAddress> listPage(PageRequest<CustAddress> prq) {
		Example<CustAddress> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(custAddressDao.findAll(ex, req));
	}

	@Override
	public List<CustAddress> getByCustomerId(String customerId) {
		return custAddressDao.findByCustomerId(customerId);
	}
}
