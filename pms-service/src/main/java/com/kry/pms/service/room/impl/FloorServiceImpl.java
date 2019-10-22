package com.kry.pms.service.room.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.room.FloorDao;
import com.kry.pms.model.persistence.room.Building;
import com.kry.pms.model.persistence.room.Floor;
import com.kry.pms.service.room.BuildingService;
import com.kry.pms.service.room.FloorService;
import com.kry.pms.service.room.GuestRoomService;

@Service
public class FloorServiceImpl implements FloorService {
	@Autowired
	FloorDao floorDao;
	@Autowired
	GuestRoomService guestRoomService;
	@Autowired
	BuildingService buildingService;

	@Override
	public Floor add(Floor floor) {
		return floorDao.saveAndFlush(floor);
	}

	@Override
	public void delete(String id) {
		Floor floor = floorDao.findById(id).get();
		if (floor != null) {
			floor.setDeleted(Constants.DELETED_TRUE);
			floorDao.saveAndFlush(floor);
		}
	}

	@Override
	public DtoResponse<Floor> deleteWithCheck(String id) {
		DtoResponse<Floor> rep = new DtoResponse<>();
		Floor floor = floorDao.findById(id).get();
		if (floor != null) {
			long count = guestRoomService.findCountByFloor(floor);
			if (count == 0) {
				floor.setDeleted(Constants.DELETED_TRUE);
				floorDao.saveAndFlush(floor);
			} else {
				rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
				rep.setCode(Constants.CODE_SHOW_LEVEL_WARING);
				rep.setMessage("当前楼层客房数不为0，请先删除客房");
			}
		}
		return rep;
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
		return floorDao.findByHotelCodeAndStatus(code, Constants.Status.NORMAL);
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


	@Override
	public List<Floor> findByBuildingId(String buildingId, int deleted) {
		Building building = buildingService.findById(buildingId);
		Floor floor = new Floor();
		floor.setDeleted(deleted);
		floor.setBuilding(building);
		Example<Floor> ex = Example.of(floor);
		return floorDao.findAll(ex);
	}

}
