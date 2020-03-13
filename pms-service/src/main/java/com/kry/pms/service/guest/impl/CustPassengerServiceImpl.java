package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustPassengerDao;
import com.kry.pms.dao.guest.VipTypeDao;
import com.kry.pms.model.persistence.guest.CustPassenger;
import com.kry.pms.model.persistence.guest.VipType;
import com.kry.pms.service.guest.CustPassengerService;
import com.kry.pms.service.guest.VipTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustPassengerServiceImpl implements CustPassengerService {
	@Autowired
	CustPassengerDao custPassengerDao;


	@Override
	public CustPassenger add(CustPassenger entity) {
		if(entity.getCustomerId() == null && entity.getPassengerId() != null){
			List<CustPassenger> list = custPassengerDao.findByCustomerIdAndPassengerId(entity.getCustomerId(), entity.getPassengerId());
			if(list != null && !list.isEmpty()){
				return null;
			}
			return custPassengerDao.saveAndFlush(entity);
		}else{
			return null;
		}
	}

	@Override
	public void delete(String id) {
		custPassengerDao.deleteById(id);
	}

	@Override
	public CustPassenger modify(CustPassenger custPassenger) {
		return custPassengerDao.saveAndFlush(custPassenger);
	}

	@Override
	public CustPassenger findById(String id) {
		return custPassengerDao.getOne(id);
	}

	@Override
	public List<CustPassenger> getAllByHotelCode(String code) {
		return null;
	}

	@Override
	public PageResponse<CustPassenger> listPage(PageRequest<CustPassenger> prq) {
		Example<CustPassenger> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(custPassengerDao.findAll(ex, req));
	}

	@Override
	public List<CustPassenger> getByCustomerId(String customerId) {
		return custPassengerDao.findByCustomerId(customerId);
	}

	@Override
	public List<Map<String, Object>> getPassengerList(String customerId) {
		return custPassengerDao.getPassengerList(customerId);
	}
}
