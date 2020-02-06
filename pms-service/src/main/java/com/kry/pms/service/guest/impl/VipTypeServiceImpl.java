package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.VipTypeDao;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.guest.VipType;
import com.kry.pms.service.guest.VipTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VipTypeServiceImpl implements VipTypeService {
	@Autowired
	VipTypeDao vipTypeDao;


	@Override
	public VipType add(VipType entity) {
		return vipTypeDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		vipTypeDao.deleteById(id);
	}

	@Override
	public VipType modify(VipType vipType) {
		return vipTypeDao.saveAndFlush(vipType);
	}

	@Override
	public VipType findById(String id) {
		return vipTypeDao.getOne(id);
	}

	@Override
	public List<VipType> getAllByHotelCode(String code) {
		return vipTypeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<VipType> listPage(PageRequest<VipType> prq) {
		Example<VipType> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(vipTypeDao.findAll(ex, req));
	}
}
