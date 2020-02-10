package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.VipRoomTypeDao;
import com.kry.pms.model.persistence.guest.VipRoomType;
import com.kry.pms.service.guest.VipRoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VipRoomTypeServiceImpl implements VipRoomTypeService {
	@Autowired
	VipRoomTypeDao vipRoomTypeDao;


	@Override
	public VipRoomType add(VipRoomType entity) {
		return vipRoomTypeDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		vipRoomTypeDao.deleteById(id);
	}

	@Override
	public VipRoomType modify(VipRoomType vipRoomType) {
		return vipRoomTypeDao.saveAndFlush(vipRoomType);
	}

	@Override
	public VipRoomType findById(String id) {
		return vipRoomTypeDao.getOne(id);
	}

	@Override
	public List<VipRoomType> getAllByHotelCode(String code) {
		return vipRoomTypeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<VipRoomType> listPage(PageRequest<VipRoomType> prq) {
		Example<VipRoomType> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(vipRoomTypeDao.findAll(ex, req));
	}
}
