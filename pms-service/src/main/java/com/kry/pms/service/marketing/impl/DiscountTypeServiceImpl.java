package com.kry.pms.service.marketing.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.marketing.DiscountTypeDao;
import com.kry.pms.model.persistence.marketing.DiscountType;
import com.kry.pms.service.marketing.DiscountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountTypeServiceImpl implements DiscountTypeService {
	@Autowired
	DiscountTypeDao discountTypeDao;
	 
	 @Override
	public DiscountType add(DiscountType discountType) {
		return discountTypeDao.saveAndFlush(discountType);
	}

	@Override
	public void delete(String id) {
		DiscountType discountType = discountTypeDao.findById(id).get();
		if (discountType != null) {
			discountType.setDeleted(Constants.DELETED_TRUE);
		}
		discountTypeDao.saveAndFlush(discountType);
	}

	@Override
	public DiscountType modify(DiscountType discountType) {
		return discountTypeDao.saveAndFlush(discountType);
	}

	@Override
	public DiscountType findById(String id) {
		return discountTypeDao.getOne(id);
	}

	@Override
	public List<DiscountType> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return discountSchemeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<DiscountType> listPage(PageRequest<DiscountType> prq) {
		Example<DiscountType> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(discountTypeDao.findAll(ex, req));
	}

	@Override
	public List<DiscountType> getAllByHotelCode(String currentHotleCode, int deleted) {
		return discountTypeDao.findByHotelCodeAndDeleted(currentHotleCode,deleted);
	}
}
