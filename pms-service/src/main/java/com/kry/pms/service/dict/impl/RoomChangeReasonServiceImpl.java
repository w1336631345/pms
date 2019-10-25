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
import com.kry.pms.dao.dict.RoomChangeReasonDao;
import com.kry.pms.model.persistence.dict.RoomChangeReason;
import com.kry.pms.service.dict.RoomChangeReasonService;

@Service
public class  RoomChangeReasonServiceImpl implements  RoomChangeReasonService{
	@Autowired
	 RoomChangeReasonDao roomChangeReasonDao;
	 
	 @Override
	public RoomChangeReason add(RoomChangeReason roomChangeReason) {
		return roomChangeReasonDao.saveAndFlush(roomChangeReason);
	}

	@Override
	public void delete(String id) {
		RoomChangeReason roomChangeReason = roomChangeReasonDao.findById(id).get();
		if (roomChangeReason != null) {
			roomChangeReason.setDeleted(Constants.DELETED_TRUE);
		}
		roomChangeReasonDao.saveAndFlush(roomChangeReason);
	}

	@Override
	public RoomChangeReason modify(RoomChangeReason roomChangeReason) {
		return roomChangeReasonDao.saveAndFlush(roomChangeReason);
	}

	@Override
	public RoomChangeReason findById(String id) {
		return roomChangeReasonDao.getOne(id);
	}

	@Override
	public List<RoomChangeReason> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return roomChangeReasonDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<RoomChangeReason> listPage(PageRequest<RoomChangeReason> prq) {
		Example<RoomChangeReason> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomChangeReasonDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
