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
import com.kry.pms.dao.dict.RoomRepairReasonDao;
import com.kry.pms.model.persistence.dict.RoomRepairReason;
import com.kry.pms.service.dict.RoomRepairReasonService;

@Service
public class  RoomRepairReasonServiceImpl implements  RoomRepairReasonService{
	@Autowired
	 RoomRepairReasonDao roomRepairReasonDao;
	 
	 @Override
	public RoomRepairReason add(RoomRepairReason roomRepairReason) {
		return roomRepairReasonDao.saveAndFlush(roomRepairReason);
	}

	@Override
	public void delete(String id) {
		RoomRepairReason roomRepairReason = roomRepairReasonDao.findById(id).get();
		if (roomRepairReason != null) {
			roomRepairReason.setDeleted(Constants.DELETED_TRUE);
		}
		modify(roomRepairReason);
	}

	@Override
	public RoomRepairReason modify(RoomRepairReason roomRepairReason) {
		return roomRepairReasonDao.saveAndFlush(roomRepairReason);
	}

	@Override
	public RoomRepairReason findById(String id) {
		return roomRepairReasonDao.getOne(id);
	}

	@Override
	public List<RoomRepairReason> getAllByHotelCode(String code) {
		return roomRepairReasonDao.findByHotelCodeAndDeleted(code,Constants.DELETED_FALSE);
	}

	@Override
	public PageResponse<RoomRepairReason> listPage(PageRequest<RoomRepairReason> prq) {
		Example<RoomRepairReason> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomRepairReasonDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
