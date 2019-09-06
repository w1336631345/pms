﻿package com.kry.pms.service.busi.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.CheckInRecordDao;
import com.kry.pms.model.http.request.busi.CheckInBo;
import com.kry.pms.model.http.request.busi.CheckInItemBo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.guest.CustomerService;
import com.kry.pms.service.room.GuestRoomService;
import com.kry.pms.service.room.GuestRoomStatusService;
import com.kry.pms.service.room.RoomStatisticsService;
import com.kry.pms.service.room.RoomStatusQuantityService;
import com.kry.pms.service.room.RoomTypeQuantityService;
import com.kry.pms.service.sys.SystemConfigService;

@Service
public class CheckInRecordServiceImpl implements CheckInRecordService {
	@Autowired
	CheckInRecordDao checkInRecordDao;
	@Autowired
	RoomRecordService roomRecordService;
	@Autowired
	GuestRoomService guestRoomService;
	@Autowired
	CustomerService customerService;
	@Autowired
	RoomStatisticsService roomStatisticsService;
	@Autowired
	GuestRoomStatusService guestRoomStatausService;
	@Autowired
	SystemConfigService systemConfigService;
	@Autowired
	RoomTypeQuantityService roomTypeQuantityService;
	@Autowired
	RoomStatusQuantityService roomStatusQuantityService;

	@Override
	public CheckInRecord add(CheckInRecord checkInRecord) {
		return checkInRecordDao.saveAndFlush(checkInRecord);
	}

	@Override
	public void delete(String id) {
		CheckInRecord checkInRecord = checkInRecordDao.findById(id).get();
		if (checkInRecord != null) {
			checkInRecord.setDeleted(Constants.DELETED_TRUE);
		}
		modify(checkInRecord);
	}

	@Override
	public CheckInRecord modify(CheckInRecord checkInRecord) {
		return checkInRecordDao.saveAndFlush(checkInRecord);
	}

	@Override
	public CheckInRecord findById(String id) {
		return checkInRecordDao.getOne(id);
	}

	@Override
	public List<CheckInRecord> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return checkInRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CheckInRecord> listPage(PageRequest<CheckInRecord> prq) {
		Example<CheckInRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(checkInRecordDao.findAll(ex, req));
	}

	@Override
	public void checkIn(CheckInBo checkInBo, DtoResponse<List<CheckInRecord>> rep) {
		for (CheckInItemBo ciib : checkInBo.getItems()) {
			GuestRoom gr = guestRoomService.findById(ciib.getRoomId());
			LocalDateTime time = LocalDateTime.now();
			ArrayList<CheckInRecord> list = createCheckInRecord(ciib, gr, time);
			// TODO check is group
			roomStatisticsService.checkIn(gr, time, ciib.getDays(), false);

		}
	}

	private ArrayList<CheckInRecord> createCheckInRecord(CheckInItemBo ciib, GuestRoom gr, LocalDateTime time) {
		ArrayList<CheckInRecord> list = new ArrayList<>();
		CheckInRecord cir = null;
		Customer customer = null;
		LocalDate startDate = time.toLocalDate();
		StringBuilder sb = new StringBuilder();
		LocalTime criticalTime = systemConfigService.getCriticalTime(gr.getHotelCode());
		if (time.toLocalTime().isBefore(criticalTime)) {
			startDate = startDate.plusDays(-1);
		}
		for (int i = 0; i < ciib.getGuests().size(); i++) {
			cir = new CheckInRecord();
			cir.setArriveTime(time);
			ciib.setHotelCode(gr.getHotelCode());
			BeanUtils.copyProperties(ciib, cir);
			customer = customerService.createOrGetCustomer(ciib.getGuests().get(i));
			cir.setCustomer(customer);
			cir.setGuestRoom(gr);
			cir.setStartDate(startDate);
			list.add(add(cir));
			roomRecordService.createRoomRecord(cir);
			sb.append(customer.getGuestInfo().getName());
			sb.append(" ");
		}
		guestRoomStatausService.checkIn(gr, startDate, sb.toString(), false, false, false, false, false);
		roomTypeQuantityService.checkIn(gr,startDate,ciib.getDays());
		return list;
	}

	@Override
	public List<CheckInRecord> checkOut(String roomId) {
		// TODO Auto-generated method stub
		return null;
	}

}