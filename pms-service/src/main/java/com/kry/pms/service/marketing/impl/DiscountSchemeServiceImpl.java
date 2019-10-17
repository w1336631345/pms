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
import com.kry.pms.dao.marketing.DiscountSchemeDao;
import com.kry.pms.model.persistence.marketing.DiscountScheme;
import com.kry.pms.service.marketing.DiscountSchemeService;

@Service
public class  DiscountSchemeServiceImpl implements  DiscountSchemeService{
	@Autowired
	 DiscountSchemeDao discountSchemeDao;
	 
	 @Override
	public DiscountScheme add(DiscountScheme discountScheme) {
		return discountSchemeDao.saveAndFlush(discountScheme);
	}

	@Override
	public void delete(String id) {
		DiscountScheme discountScheme = discountSchemeDao.findById(id).get();
		if (discountScheme != null) {
			discountScheme.setDeleted(Constants.DELETED_TRUE);
		}
		discountSchemeDao.saveAndFlush(discountScheme);
	}

	@Override
	public DiscountScheme modify(DiscountScheme discountScheme) {
		return discountSchemeDao.saveAndFlush(discountScheme);
	}

	@Override
	public DiscountScheme findById(String id) {
		return discountSchemeDao.getOne(id);
	}

	@Override
	public List<DiscountScheme> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return discountSchemeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<DiscountScheme> listPage(PageRequest<DiscountScheme> prq) {
		Example<DiscountScheme> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(discountSchemeDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
