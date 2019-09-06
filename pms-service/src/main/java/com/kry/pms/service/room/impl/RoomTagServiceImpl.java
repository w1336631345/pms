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
import com.kry.pms.dao.room.RoomTagDao;
import com.kry.pms.model.persistence.room.RoomTag;
import com.kry.pms.service.room.RoomTagService;

@Service
public class  RoomTagServiceImpl implements  RoomTagService{
	@Autowired
	 RoomTagDao roomTagDao;
	 
	 @Override
	public RoomTag add(RoomTag roomTag) {
		return roomTagDao.saveAndFlush(roomTag);
	}

	@Override
	public void delete(String id) {
		RoomTag roomTag = roomTagDao.findById(id).get();
		if (roomTag != null) {
			roomTag.setDeleted(Constants.DELETED_TRUE);
		}
		roomTagDao.saveAndFlush(roomTag);
	}

	@Override
	public RoomTag modify(RoomTag roomTag) {
		return roomTagDao.saveAndFlush(roomTag);
	}

	@Override
	public RoomTag findById(String id) {
		return roomTagDao.getOne(id);
	}

	@Override
	public List<RoomTag> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return roomTagDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<RoomTag> listPage(PageRequest<RoomTag> prq) {
		Example<RoomTag> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomTagDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
