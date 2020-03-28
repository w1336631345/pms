package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.ProtocolTypeDao;
import com.kry.pms.model.persistence.guest.ProtocolType;
import com.kry.pms.service.guest.ProtocolTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProtocolTypeServiceImpl implements ProtocolTypeService {
	@Autowired
	ProtocolTypeDao protocolTypeDao;


	@Override
	public ProtocolType add(ProtocolType entity) {
		return protocolTypeDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		protocolTypeDao.deleteById(id);
	}

	@Override
	public ProtocolType modify(ProtocolType vipType) {
		return protocolTypeDao.saveAndFlush(vipType);
	}

	@Override
	public ProtocolType findById(String id) {
		return protocolTypeDao.getOne(id);
	}

	@Override
	public List<ProtocolType> getAllByHotelCode(String code) {
		return protocolTypeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<ProtocolType> listPage(PageRequest<ProtocolType> prq) {
		Example<ProtocolType> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(protocolTypeDao.findAll(ex, req));
	}
}
