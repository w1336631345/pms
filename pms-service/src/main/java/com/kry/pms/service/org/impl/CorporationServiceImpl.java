package com.kry.pms.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.org.CorporationDao;
import com.kry.pms.model.persistence.org.Corporation;
import com.kry.pms.service.org.CorporationService;

@Service
public class  CorporationServiceImpl implements  CorporationService{
	@Autowired
	 CorporationDao corporationDao;
	 
	 @Override
	public Corporation add(Corporation corporation) {
		return corporationDao.saveAndFlush(corporation);
	}

	@Override
	public void delete(String id) {
		Corporation corporation = corporationDao.findById(id).get();
		if (corporation != null) {
			corporation.setDeleted(Constants.DELETED_TRUE);
		}
		corporationDao.saveAndFlush(corporation);
	}

	@Override
	public Corporation modify(Corporation corporation) {
		return corporationDao.saveAndFlush(corporation);
	}

	@Override
	public Corporation findById(String id) {
		return corporationDao.getOne(id);
	}

	@Override
	public List<Corporation> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return corporationDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Corporation> listPage(PageRequest<Corporation> prq) {
		Example<Corporation> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(corporationDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
