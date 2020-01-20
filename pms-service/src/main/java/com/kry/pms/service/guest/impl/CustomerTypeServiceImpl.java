package com.kry.pms.service.guest.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustomerDao;
import com.kry.pms.dao.guest.CustomerTypeDao;
import com.kry.pms.model.http.request.busi.GuestInfoBo;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.guest.CustomerType;
import com.kry.pms.model.persistence.guest.GuestInfo;
import com.kry.pms.service.guest.CustomerService;
import com.kry.pms.service.guest.CustomerTypeService;
import com.kry.pms.service.guest.GuestInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerTypeServiceImpl implements CustomerTypeService {
	@Autowired
	CustomerTypeDao customerTypeDao;

	@Override
	public CustomerType add(CustomerType entity) {
		return null;
	}

	@Override
	public void delete(String id) {

	}

	@Override
	public CustomerType modify(CustomerType customerType) {
		return null;
	}

	@Override
	public CustomerType findById(String id) {
		return null;
	}

	@Override
	public List<CustomerType> getAllByHotelCode(String code) {
		return customerTypeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CustomerType> listPage(PageRequest<CustomerType> prq) {
		return null;
	}
}
