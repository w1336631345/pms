package com.kry.pms.service.room.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.room.BuildingDao;
import com.kry.pms.model.persistence.room.Building;
import com.kry.pms.service.room.BuildingService;

@Service
public class  BuildingServiceImpl implements  BuildingService{
	@Autowired
	 BuildingDao buildingDao;
	 
	 @Override
	public Building add(Building building) {
		return buildingDao.saveAndFlush(building);
	}

	@Override
	public void delete(String id) {
		Building building = buildingDao.findById(id).get();
		if (building != null) {
			building.setDeleted(true);
		}
		buildingDao.saveAndFlush(building);
	}

	@Override
	public Building modify(Building building) {
		return buildingDao.saveAndFlush(building);
	}

	@Override
	public Building findById(String id) {
		return buildingDao.getOne(id);
	}

	@Override
	public List<Building> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return buildingDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Building> listPage(PageRequest<Building> prq) {
		Example<Building> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(buildingDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
