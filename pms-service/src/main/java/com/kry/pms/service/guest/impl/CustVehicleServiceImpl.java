package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustInvoiceDao;
import com.kry.pms.dao.guest.CustVehicleDao;
import com.kry.pms.model.persistence.guest.CustInvoice;
import com.kry.pms.model.persistence.guest.CustVehicle;
import com.kry.pms.service.guest.CustInvoiceService;
import com.kry.pms.service.guest.CustVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustVehicleServiceImpl implements CustVehicleService {
	@Autowired
	CustVehicleDao custVehicleDao;


	@Override
	public CustVehicle add(CustVehicle entity) {
		return custVehicleDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		custVehicleDao.deleteById(id);
	}

	@Override
	public CustVehicle modify(CustVehicle custPassenger) {
		return custVehicleDao.saveAndFlush(custPassenger);
	}

	@Override
	public CustVehicle findById(String id) {
		return custVehicleDao.getOne(id);
	}

	@Override
	public List<CustVehicle> getAllByHotelCode(String code) {
		return custVehicleDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CustVehicle> listPage(PageRequest<CustVehicle> prq) {
		Example<CustVehicle> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(custVehicleDao.findAll(ex, req));
	}

	@Override
	public List<CustVehicle> getByCustomerId(String customerId) {
		return custVehicleDao.findByCustomerId(customerId);
	}
}
