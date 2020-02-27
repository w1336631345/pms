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
import com.kry.pms.dao.dict.DictDataDao;
import com.kry.pms.model.persistence.dict.DictData;
import com.kry.pms.service.dict.DictDataService;

@Service
public class  DictDataServiceImpl implements  DictDataService{
	@Autowired
	 DictDataDao dictDataDao;
	 
	 @Override
	public DictData add(DictData dictData) {
		return dictDataDao.saveAndFlush(dictData);
	}

	@Override
	public void delete(String id) {
		DictData dictData = dictDataDao.findById(id).get();
		if (dictData != null) {
			dictData.setDeleted(Constants.DELETED_TRUE);
		}
		modify(dictData);
	}

	@Override
	public DictData modify(DictData dictData) {
		return dictDataDao.saveAndFlush(dictData);
	}

	@Override
	public DictData findById(String id) {
		return dictDataDao.getOne(id);
	}

	@Override
	public List<DictData> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return dictDataDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<DictData> listPage(PageRequest<DictData> prq) {
		Example<DictData> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(dictDataDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
