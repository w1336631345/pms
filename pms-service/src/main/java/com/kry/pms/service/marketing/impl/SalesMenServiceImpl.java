package com.kry.pms.service.marketing.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.marketing.SalesMenDao;
import com.kry.pms.model.persistence.marketing.SalesMen;
import com.kry.pms.service.marketing.SalesMenService;

@Service
public class  SalesMenServiceImpl implements  SalesMenService{
	@Autowired
	 SalesMenDao salesMenDao;
	 
	 @Override
	public SalesMen add(SalesMen salesMen) {
		return salesMenDao.saveAndFlush(salesMen);
	}

	@Override
	public void delete(String id) {
		SalesMen salesMen = salesMenDao.findById(id).get();
		if (salesMen != null) {
			salesMen.setDeleted(Constants.DELETED_TRUE);
		}
		modify(salesMen);
	}

	@Override
	public SalesMen modify(SalesMen salesMen) {
		return salesMenDao.saveAndFlush(salesMen);
	}

	@Override
	public SalesMen findById(String id) {
		return salesMenDao.getOne(id);
	}

	@Override
	public List<SalesMen> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return salesMenDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<SalesMen> listPage(PageRequest<SalesMen> prq) {
		Example<SalesMen> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(salesMenDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
