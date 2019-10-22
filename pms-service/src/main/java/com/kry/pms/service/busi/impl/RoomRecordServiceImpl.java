package com.kry.pms.service.busi.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.RoomRecordDao;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.service.busi.BillItemService;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.busi.BookingRecordService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.guest.CustomerService;
import com.kry.pms.service.room.GuestRoomService;
import com.kry.pms.service.room.GuestRoomStatusService;
import com.kry.pms.service.room.RoomStatisticsService;
import com.kry.pms.service.room.RoomStatusQuantityService;
import com.kry.pms.service.sys.SystemConfigService;

@Service
public class RoomRecordServiceImpl implements RoomRecordService {
	@Autowired
	RoomRecordDao roomRecordDao;
	@Autowired
	BookingRecordService bookingRecordService;
	@Autowired
	GuestRoomService guestRoomService;
	@Autowired
	SystemConfigService systemConfigService;
	@Autowired
	RoomStatisticsService roomStatisticsService;
	@Autowired
	CustomerService customerService;
	@Autowired
	BillService billService;

	@Override
	public RoomRecord add(RoomRecord roomRecord) {
		return roomRecordDao.saveAndFlush(roomRecord);
	}

	@Override
	public void delete(String id) {
		RoomRecord roomRecord = roomRecordDao.findById(id).get();
		if (roomRecord != null) {
			roomRecord.setDeleted(Constants.DELETED_TRUE);
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
		return null;// 默认不实现
		// return roomRecordDao.findByHotelCode(code);
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

	@Override
	public void createRoomRecord(CheckInRecord cir) {
		RoomRecord rr = null;
		for (int i = 0; i < cir.getDays(); i++) {
			rr = new RoomRecord();
			rr.setCheckInRecord(cir);
			rr.setCustomer(cir.getCustomer());
			rr.setGuestRoom(cir.getGuestRoom());
			rr.setRecordDate(cir.getStartDate().plusDays(i));
			add(rr);
		}
	}

	@Override
	public void dailyVerify(String hotelCode, LocalDate recordDate, DailyVerify dv) {
		List<RoomRecord> list = roomRecordDao.findByHotelCodeAndRecordDate(hotelCode, recordDate);
		if (list != null && !list.isEmpty()) {
			for (RoomRecord rr : list) {
				rr.setStatus(Constants.Status.ROOM_RECORD_STATUS_DAILY_VERIFY_PASS);
				rr.setDailyVerify(dv);
				modify(rr);
//				billService.billEntry(rr,recordDate);
			}
		}
	}

	@Override
	public void checkOut(List<CheckInRecord> crs) {
		// TODO Auto-generated method stub

	}

}
