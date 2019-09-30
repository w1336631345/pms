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
import com.kry.pms.dao.dict.RoomLockReasonDao;
import com.kry.pms.model.persistence.dict.RoomLockReason;
import com.kry.pms.service.dict.RoomLockReasonService;

@Service
public class  RoomLockReasonServiceImpl implements  RoomLockReasonService{
	@Autowired
	 RoomLockReasonDao roomLockReasonDao;
	 
	 @Override
	public RoomLockReason add(RoomLockReason roomLockReason) {
		return roomLockReasonDao.saveAndFlush(roomLockReason);
	}

	@Override
	public void delete(String id) {
		RoomLockReason roomLockReason = roomLockReasonDao.findById(id).get();
		if (roomLockReason != null) {
			roomLockReason.setDeleted(Constants.DELETED_TRUE);
		}
		modify(roomLockReason);
	}

	@Override
	public RoomLockReason modify(RoomLockReason roomLockReason) {
		return roomLockReasonDao.saveAndFlush(roomLockReason);
	}

	@Override
	public RoomLockReason findById(String id) {
		return roomLockReasonDao.getOne(id);
	}

	@Override
	public List<RoomLockReason> getAllByHotelCode(String code) {
//		return null;//默认不实现
		return roomLockReasonDao.findByHotelCodeAndDeleted(code,Constants.DELETED_FALSE);
	}

	@Override
	public PageResponse<RoomLockReason> listPage(PageRequest<RoomLockReason> prq) {
		Example<RoomLockReason> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomLockReasonDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
