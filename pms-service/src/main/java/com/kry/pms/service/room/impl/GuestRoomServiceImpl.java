package com.kry.pms.service.room.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.room.GuestRoomDao;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.room.GuestRoomService;

@Service
public class  GuestRoomServiceImpl implements  GuestRoomService{
	@Autowired
	 GuestRoomDao guestRoomDao;
	 
	 @Override
	public GuestRoom add(GuestRoom guestRoom) {
		return guestRoomDao.saveAndFlush(guestRoom);
	}

	@Override
	public void delete(String id) {
		GuestRoom guestRoom = guestRoomDao.findById(id).get();
		if (guestRoom != null) {
			guestRoom.setDeleted(true);
		}
		guestRoomDao.saveAndFlush(guestRoom);
	}

	@Override
	public GuestRoom modify(GuestRoom guestRoom) {
		return guestRoomDao.saveAndFlush(guestRoom);
	}

	@Override
	public GuestRoom findById(String id) {
		return guestRoomDao.getOne(id);
	}

	@Override
	public List<GuestRoom> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return guestRoomDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<GuestRoom> listPage(PageRequest<GuestRoom> prq) {
		Example<GuestRoom> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(guestRoomDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
