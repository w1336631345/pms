package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustFeaturesDao;
import com.kry.pms.dao.guest.CustVehicleDao;
import com.kry.pms.model.persistence.guest.CustFeatures;
import com.kry.pms.model.persistence.guest.CustVehicle;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.service.guest.CustFeaturesService;
import com.kry.pms.service.guest.CustVehicleService;
import com.kry.pms.service.guest.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustFeaturesServiceImpl implements CustFeaturesService {
	@Autowired
	CustFeaturesDao custFeaturesDao;
	@Autowired
	CustomerService customerService;


	@Override
	public CustFeatures add(CustFeatures entity) {
		if(entity.getAutograph() != null || entity.getPhoto() != null){
			Customer customer = customerService.findById(entity.getCustomerId());
			customer.setAutograph(entity.getAutograph());
			customer.setPhoto(entity.getPhoto());
			customerService.modify(customer);
		}
		return custFeaturesDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		custFeaturesDao.deleteById(id);
	}

	@Override
	public CustFeatures modify(CustFeatures custFeatures) {
		if(custFeatures.getAutograph() != null || custFeatures.getPhoto() != null){
			Customer customer = customerService.findById(custFeatures.getCustomerId());
			customer.setAutograph(custFeatures.getAutograph());
			customer.setPhoto(custFeatures.getPhoto());
			customerService.modify(customer);
		}
		return custFeaturesDao.saveAndFlush(custFeatures);
	}

	@Override
	public CustFeatures findById(String id) {
		return custFeaturesDao.getOne(id);
	}

	@Override
	public List<CustFeatures> getAllByHotelCode(String code) {
		return custFeaturesDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CustFeatures> listPage(PageRequest<CustFeatures> prq) {
		Example<CustFeatures> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(custFeaturesDao.findAll(ex, req));
	}

	@Override
	public List<CustFeatures> getByCustomerId(String customerId) {
		return custFeaturesDao.findByCustomerId(customerId);
	}
}
