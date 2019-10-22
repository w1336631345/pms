package com.kry.pms.service.room.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.room.RoomStatusQuantityDao;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomStatusQuantity;
import com.kry.pms.service.room.RoomStatusQuantityService;

@Service
public class RoomStatusQuantityServiceImpl implements RoomStatusQuantityService {
	@Autowired
	RoomStatusQuantityDao roomStatusQuantityDao;

	@Override
	public RoomStatusQuantity add(RoomStatusQuantity roomStatusQuantity) {
		return roomStatusQuantityDao.saveAndFlush(roomStatusQuantity);
	}

	@Override
	public void delete(String id) {
		RoomStatusQuantity roomStatusQuantity = roomStatusQuantityDao.findById(id).get();
		if (roomStatusQuantity != null) {
			roomStatusQuantity.setDeleted(Constants.DELETED_TRUE);
		}
		roomStatusQuantityDao.saveAndFlush(roomStatusQuantity);
	}

	@Override
	public RoomStatusQuantity modify(RoomStatusQuantity roomStatusQuantity) {
		return roomStatusQuantityDao.saveAndFlush(roomStatusQuantity);
	}

	@Override
	public RoomStatusQuantity findById(String id) {
		return roomStatusQuantityDao.getOne(id);
	}

	@Override
	public List<RoomStatusQuantity> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return roomStatusQuantityDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<RoomStatusQuantity> listPage(PageRequest<RoomStatusQuantity> prq) {
		Example<RoomStatusQuantity> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomStatusQuantityDao.findAll(ex, req));
	}

	@Override
	public void checkIn(GuestRoom gr, String oldStatus) {
		if (Constants.Status.ROOM_STATUS_VACANT_CLEAN.equals(oldStatus)) {
			transformRoomStatusQuantity(gr.getHotelCode(), oldStatus, Constants.Status.ROOM_STATUS_OCCUPY_CLEAN, 1);
		} else {
			transformRoomStatusQuantity(gr.getHotelCode(), oldStatus, Constants.Status.ROOM_STATUS_OCCUPY_DIRTY, 1);
		}
	}
	@Override
	public void transformRoomStatusQuantity(String hotelCode, String oldStatus, String newStatus, int quantity) {
		roomStatusQuantityDao.plusQuantity(hotelCode, oldStatus, -quantity);
		roomStatusQuantityDao.plusQuantity(hotelCode, newStatus, quantity);

	}

}
