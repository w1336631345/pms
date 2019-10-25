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
import com.kry.pms.dao.dict.AreaDao;
import com.kry.pms.model.persistence.dict.Area;
import com.kry.pms.service.dict.AreaService;

@Service
public class  AreaServiceImpl implements  AreaService{
	@Autowired
	 AreaDao areaDao;
	 
	 @Override
	public Area add(Area area) {
		return areaDao.saveAndFlush(area);
	}

	@Override
	public void delete(String id) {
		Area area = areaDao.findById(id).get();
		if (area != null) {
			area.setDeleted(Constants.DELETED_TRUE);
		}
		areaDao.saveAndFlush(area);
	}

	@Override
	public Area modify(Area area) {
		return areaDao.saveAndFlush(area);
	}

	@Override
	public Area findById(String id) {
		return areaDao.getOne(id);
	}

	@Override
	public List<Area> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return areaDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Area> listPage(PageRequest<Area> prq) {
		Example<Area> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(areaDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
