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
import com.kry.pms.dao.goods.SetMealItemDao;
import com.kry.pms.model.persistence.goods.SetMealItem;
import com.kry.pms.service.goods.SetMealItemService;

@Service
public class  SetMealItemServiceImpl implements  SetMealItemService{
	@Autowired
	 SetMealItemDao setMealItemDao;
	 
	 @Override
	public SetMealItem add(SetMealItem setMealItem) {
		return setMealItemDao.saveAndFlush(setMealItem);
	}

	@Override
	public void delete(String id) {
		SetMealItem setMealItem = setMealItemDao.findById(id).get();
		if (setMealItem != null) {
			setMealItem.setDeleted(Constants.DELETED_TRUE);
		}
		modify(setMealItem);
	}

	@Override
	public SetMealItem modify(SetMealItem setMealItem) {
		return setMealItemDao.saveAndFlush(setMealItem);
	}

	@Override
	public SetMealItem findById(String id) {
		return setMealItemDao.getOne(id);
	}

	@Override
	public List<SetMealItem> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return setMealItemDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<SetMealItem> listPage(PageRequest<SetMealItem> prq) {
		Example<SetMealItem> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(setMealItemDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
