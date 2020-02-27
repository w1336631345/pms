package com.kry.pms.service.dict.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.dict.DictTypeDao;
import com.kry.pms.model.persistence.dict.DictType;
import com.kry.pms.service.dict.DictTypeService;

@Service
public class  DictTypeServiceImpl implements  DictTypeService{
	@Autowired
	 DictTypeDao dictTypeDao;
	 
	 @Override
	public DictType add(DictType dictType) {
		return dictTypeDao.saveAndFlush(dictType);
	}

	@Override
	public void delete(String id) {
		DictType dictType = dictTypeDao.findById(id).get();
		if (dictType != null) {
			dictType.setDeleted(Constants.DELETED_TRUE);
		}
		modify(dictType);
	}

	@Override
	public DictType modify(DictType dictType) {
		return dictTypeDao.saveAndFlush(dictType);
	}

	@Override
	public DictType findById(String id) {
		return dictTypeDao.getOne(id);
	}

	@Override
	public List<DictType> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return dictTypeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<DictType> listPage(PageRequest<DictType> prq) {
		Example<DictType> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(dictTypeDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
