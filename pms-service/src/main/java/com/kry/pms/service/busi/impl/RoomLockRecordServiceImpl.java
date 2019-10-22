package com.kry.pms.service.busi.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.RoomLockRecordDao;
import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.service.busi.RoomLockRecordService;

@Service
public class  RoomLockRecordServiceImpl implements  RoomLockRecordService{
	@Autowired
	 RoomLockRecordDao roomLockRecordDao;
	 
	 @Override
	public RoomLockRecord add(RoomLockRecord roomLockRecord) {
		return roomLockRecordDao.saveAndFlush(roomLockRecord);
	}

	@Override
	public void delete(String id) {
		RoomLockRecord roomLockRecord = roomLockRecordDao.findById(id).get();
		if (roomLockRecord != null) {
			roomLockRecord.setDeleted(Constants.DELETED_TRUE);
		}
		modify(roomLockRecord);
	}

	@Override
	public RoomLockRecord modify(RoomLockRecord roomLockRecord) {
		return roomLockRecordDao.saveAndFlush(roomLockRecord);
	}

	@Override
	public RoomLockRecord findById(String id) {
		return roomLockRecordDao.getOne(id);
	}

	@Override
	public List<RoomLockRecord> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return roomLockRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<RoomLockRecord> listPage(PageRequest<RoomLockRecord> prq) {
		Example<RoomLockRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomLockRecordDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
