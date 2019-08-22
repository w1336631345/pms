package com.kry.pms.service.room.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.room.FloorDao;
import com.kry.pms.model.persistence.room.Floor;
import com.kry.pms.service.room.FloorService;

@Service
public class  FloorServiceImpl implements  FloorService{
	@Autowired
	 FloorDao floorDao;
	 
	 @Override
	public Floor add(Floor floor) {
		return floorDao.saveAndFlush(floor);
	}

	@Override
	public void delete(String id) {
		Floor floor = floorDao.findById(id).get();
		if (floor != null) {
			floor.setDeleted(true);
		}
		floorDao.saveAndFlush(floor);
	}

	@Override
	public Floor modify(Floor floor) {
		return floorDao.saveAndFlush(floor);
	}

	@Override
	public Floor findById(String id) {
		return floorDao.getOne(id);
	}

	@Override
	public List<Floor> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return floorDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Floor> listPage(PageRequest<Floor> prq) {
		Example<Floor> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(floorDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
