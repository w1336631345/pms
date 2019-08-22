package com.kry.pms.service.busi.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.RoomRecordDao;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.service.busi.RoomRecordService;

@Service
public class  RoomRecordServiceImpl implements  RoomRecordService{
	@Autowired
	 RoomRecordDao roomRecordDao;
	 
	 @Override
	public RoomRecord add(RoomRecord roomRecord) {
		return roomRecordDao.saveAndFlush(roomRecord);
	}

	@Override
	public void delete(String id) {
		RoomRecord roomRecord = roomRecordDao.findById(id).get();
		if (roomRecord != null) {
			roomRecord.setDeleted(true);
		}
		roomRecordDao.saveAndFlush(roomRecord);
	}

	@Override
	public RoomRecord modify(RoomRecord roomRecord) {
		return roomRecordDao.saveAndFlush(roomRecord);
	}

	@Override
	public RoomRecord findById(String id) {
		return roomRecordDao.getOne(id);
	}

	@Override
	public List<RoomRecord> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return roomRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<RoomRecord> listPage(PageRequest<RoomRecord> prq) {
		Example<RoomRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomRecordDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
