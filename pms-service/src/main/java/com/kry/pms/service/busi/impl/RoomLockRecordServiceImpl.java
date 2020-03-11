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
import com.kry.pms.dao.busi.RoomLockRecordDao;
import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.model.persistence.busi.RoomRepairRecord;
import com.kry.pms.model.persistence.dict.RoomLockReason;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.GuestRoomStatus;
import com.kry.pms.service.busi.RoomLockRecordService;
import com.kry.pms.service.dict.RoomLockReasonService;

@Service
public class RoomLockRecordServiceImpl implements RoomLockRecordService {
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
		return null;// 默认不实现
		// return roomLockRecordDao.findByHotelCode(code);
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

	@Override
	public RoomLockRecord createRecord(GuestRoom gr, LocalDateTime startTime, LocalDateTime endTime,
			String reason, String endToStatus) {
		RoomLockRecord rlr = new RoomLockRecord();
		rlr.setGuestRoom(gr);
		rlr.setStartTime(startTime);
		rlr.setEndTime(endTime);
		rlr.setReason(reason);
		rlr.setEndToStatus(endToStatus);
		return rlr;
	}

	@Override
	public RoomLockRecord openLock(String id, String operationEmployeeId) {
		RoomLockRecord rlr = roomLockRecordDao.queryTopRecord(id, Constants.Status.NORMAL);
		if (rlr != null && rlr.getGuestRoom().getId().equals(id) && rlr.getStatus().equals(Constants.Status.NORMAL)) {
			rlr.setStatus(Constants.Status.CLOSE);
			modify(rlr);
		}
		return rlr;
	}
	@Override
	public List<RoomLockRecord> findByGuestRoomAndStatus(GuestRoom guestRoom, String status) {
		return roomLockRecordDao.findByGuestRoomAndStatus(guestRoom,status);
	}

}
