package com.kry.pms.service.busi.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.RoomRepairRecordDao;
import com.kry.pms.model.persistence.busi.RoomRepairRecord;
import com.kry.pms.model.persistence.dict.RoomRepairReason;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.busi.RoomRepairRecordService;

@Service
public class RoomRepairRecordServiceImpl implements RoomRepairRecordService {
	@Autowired
	RoomRepairRecordDao roomRepairRecordDao;

	@Override
	public RoomRepairRecord add(RoomRepairRecord roomRepairRecord) {
		return roomRepairRecordDao.saveAndFlush(roomRepairRecord);
	}

	@Override
	public void delete(String id) {
		RoomRepairRecord roomRepairRecord = roomRepairRecordDao.findById(id).get();
		if (roomRepairRecord != null) {
			roomRepairRecord.setDeleted(Constants.DELETED_TRUE);
		}
		modify(roomRepairRecord);
	}

	@Override
	public RoomRepairRecord modify(RoomRepairRecord roomRepairRecord) {
		return roomRepairRecordDao.saveAndFlush(roomRepairRecord);
	}

	@Override
	public RoomRepairRecord findById(String id) {
		return roomRepairRecordDao.getOne(id);
	}

	@Override
	public List<RoomRepairRecord> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return roomRepairRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<RoomRepairRecord> listPage(PageRequest<RoomRepairRecord> prq) {
		Example<RoomRepairRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomRepairRecordDao.findAll(ex, req));
	}

	@Override
	public RoomRepairRecord createRecord(GuestRoom gr, LocalDateTime startTime, LocalDateTime endTime,
			String rpr, String endToStatus) {
		RoomRepairRecord rlr = new RoomRepairRecord();
		rlr.setGuestRoom(gr);
		rlr.setStartTime(startTime);
		rlr.setEndTime(endTime);
		rlr.setReason(rpr);
		rlr.setEndToStatus(endToStatus);
		return rlr;
	}

	@Override
	public RoomRepairRecord openRepair(String id, String operationEmployeeId) {
		RoomRepairRecord rlr = roomRepairRecordDao.queryTopRecord(id, Constants.Status.NORMAL);
		if (rlr != null && rlr.getGuestRoom().getId().equals(id) && rlr.getStatus().equals(Constants.Status.NORMAL)) {
			rlr.setStatus(Constants.Status.CLOSE);
			modify(rlr);
		}
		return rlr;
	}

	@Override
	public List<RoomRepairRecord> findByGuestRoomAndStatus(GuestRoom guestRoom, String status) {
		return roomRepairRecordDao.findByGuestRoomAndStatus(guestRoom,status);
	}

}
