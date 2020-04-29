package com.kry.pms.service.log.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.log.InterfaceUseLogDao;
import com.kry.pms.model.persistence.log.InterfaceUseLog;
import com.kry.pms.service.log.InterfaceUseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterfaceUseLogServiceImpl implements InterfaceUseLogService {
	@Autowired
	InterfaceUseLogDao interfaceUseLogDao;
	 
	 @Override
	public InterfaceUseLog add(InterfaceUseLog interfaceUseLog) {
		return interfaceUseLogDao.saveAndFlush(interfaceUseLog);
	}

	@Override
	public void delete(String id) {
		interfaceUseLogDao.deleteById(id);
	}

	@Override
	public InterfaceUseLog modify(InterfaceUseLog interfaceUseLog) {
		return interfaceUseLogDao.saveAndFlush(interfaceUseLog);
	}

	@Override
	public InterfaceUseLog findById(String id) {
		return interfaceUseLogDao.getOne(id);
	}

	@Override
	public List<InterfaceUseLog> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return interfaceUseLogDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<InterfaceUseLog> listPage(PageRequest<InterfaceUseLog> prq) {
		Example<InterfaceUseLog> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		Page<InterfaceUseLog> list = interfaceUseLogDao.findAll(ex, req);
		return convent(list);
	}
	 
	 
	 
	 
}
