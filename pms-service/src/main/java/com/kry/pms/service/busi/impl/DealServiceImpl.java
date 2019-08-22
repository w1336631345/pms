package com.kry.pms.service.busi.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.DealDao;
import com.kry.pms.model.persistence.busi.Deal;
import com.kry.pms.service.busi.DealService;

@Service
public class  DealServiceImpl implements  DealService{
	@Autowired
	 DealDao dealDao;
	 
	 @Override
	public Deal add(Deal deal) {
		return dealDao.saveAndFlush(deal);
	}

	@Override
	public void delete(String id) {
		Deal deal = dealDao.findById(id).get();
		if (deal != null) {
			deal.setDeleted(true);
		}
		dealDao.saveAndFlush(deal);
	}

	@Override
	public Deal modify(Deal deal) {
		return dealDao.saveAndFlush(deal);
	}

	@Override
	public Deal findById(String id) {
		return dealDao.getOne(id);
	}

	@Override
	public List<Deal> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return dealDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Deal> listPage(PageRequest<Deal> prq) {
		Example<Deal> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(dealDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
