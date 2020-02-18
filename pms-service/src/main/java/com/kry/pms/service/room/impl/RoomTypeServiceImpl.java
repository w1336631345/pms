package com.kry.pms.service.room.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.room.RoomTypeDao;
import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.service.room.RoomTypeQuantityService;
import com.kry.pms.service.room.RoomTypeService;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
	@Autowired
	RoomTypeDao roomTypeDao;
	@Autowired
	RoomTypeQuantityService roomTypeQuantityService;

	@Transactional
	@Override
//	@CacheEvict(value = "room_type", key = "targetClass+#p0.hotelCode")
	public RoomType add(RoomType roomType) {
		roomType = roomTypeDao.saveAndFlush(roomType);
		roomTypeQuantityService.initNewType(roomType);
		return roomType;
	}

	@Override
	public void delete(String id) {
		RoomType roomType = roomTypeDao.findById(id).get();
		if (roomType != null) {
			roomType.setDeleted(Constants.DELETED_TRUE);
		}
		modify(roomType);
	}

//	@CacheEvict(value = "room_type", key = "targetClass+#p0.hotelCode")
	@Override
	public RoomType modify(RoomType roomType) {
		return roomTypeDao.saveAndFlush(roomType);
	}

	@Override
	public RoomType findById(String id) {
		return roomTypeDao.getOne(id);
	}

	@Override
//	@Cacheable(value = "room_type", key = "targetClass+#p0")
	public List<RoomType> getAllByHotelCode(String code) {
		return roomTypeDao.findByHotelCode(code);
	}

	@Override
	public List<RoomType> getAllByHotelCode(String code, int deleted) {
		return roomTypeDao.findByHotelCodeAndDeleted(code, deleted);
	}

	@Override
	public PageResponse<RoomType> listPage(PageRequest<RoomType> prq) {
		Example<RoomType> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomTypeDao.findAll(ex, req));
	}

	@Override
	public void plusRoomQuantity(RoomType roomType, int size) {
		roomType = findById(roomType.getId());
		roomType.setRoomCount(roomType.getRoomCount() + size);
		roomTypeQuantityService.addRoomQuantity(roomType, size);

	}

}
