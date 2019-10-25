package com.kry.pms.service.goods.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.goods.SetMealDao;
import com.kry.pms.model.persistence.goods.SetMeal;
import com.kry.pms.service.goods.SetMealService;

@Service
public class  SetMealServiceImpl implements  SetMealService{
	@Autowired
	 SetMealDao setMealDao;
	 
	 @Override
	public SetMeal add(SetMeal setMeal) {
		return setMealDao.saveAndFlush(setMeal);
	}

	@Override
	public void delete(String id) {
		SetMeal setMeal = setMealDao.findById(id).get();
		if (setMeal != null) {
			setMeal.setDeleted(Constants.DELETED_TRUE);
		}
		modify(setMeal);
	}

	@Override
	public SetMeal modify(SetMeal setMeal) {
		return setMealDao.saveAndFlush(setMeal);
	}

	@Override
	public SetMeal findById(String id) {
		return setMealDao.getOne(id);
	}

	@Override
	public List<SetMeal> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return setMealDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<SetMeal> listPage(PageRequest<SetMeal> prq) {
		Example<SetMeal> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(setMealDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
