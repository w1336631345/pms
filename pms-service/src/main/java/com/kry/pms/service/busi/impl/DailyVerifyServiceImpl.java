package com.kry.pms.service.busi.impl;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.DailyVerifyDao;
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.service.busi.DailyVerifyService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.room.GuestRoomStatusService;
import com.kry.pms.service.room.RoomStatusQuantityService;

@Service
public class DailyVerifyServiceImpl implements DailyVerifyService {
	@Autowired
	DailyVerifyDao dailyVerifyDao;
	@Autowired
	RoomRecordService roomRecordService;
	@Autowired
	GuestRoomStatusService guestRoomStatusService;
	@Autowired
	RoomStatusQuantityService roomStatusQuantityService;

	@Override
	public DailyVerify add(DailyVerify dailyVerify) {
		return dailyVerifyDao.saveAndFlush(dailyVerify);
	}

	@Override
	public void delete(String id) {
		DailyVerify dailyVerify = dailyVerifyDao.findById(id).get();
		if (dailyVerify != null) {
			dailyVerify.setDeleted(Constants.DELETED_TRUE);
		}
		modify(dailyVerify);
	}

	@Override
	public DailyVerify modify(DailyVerify dailyVerify) {
		return dailyVerifyDao.saveAndFlush(dailyVerify);
	}

	@Override
	public DailyVerify findById(String id) {
		return dailyVerifyDao.getOne(id);
	}

	@Override
	public List<DailyVerify> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return dailyVerifyDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<DailyVerify> listPage(PageRequest<DailyVerify> prq) {
		Example<DailyVerify> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(dailyVerifyDao.findAll(ex, req));
	}

	@Transactional
	@Override
	public void autoDailyVerify(String hotelCode) {
		DailyVerify dv = new DailyVerify();
		dv.setHotelCode(hotelCode);
		dv.setVerifyDate(LocalDate.now().plusDays(-1));
		dv.setType(Constants.Type.AUTO_DAILY_VERIFY);
		dv = add(dv);
		roomRecordService.dailyVerify(hotelCode, dv.getVerifyDate(), dv);
		int count = guestRoomStatusService.batchChangeRoomStatus(hotelCode, Constants.Status.ROOM_STATUS_OCCUPY_CLEAN,
				Constants.Status.ROOM_STATUS_OCCUPY_DIRTY);
		if (count > 0) {
			roomStatusQuantityService.transformRoomStatusQuantity(hotelCode, Constants.Status.ROOM_STATUS_OCCUPY_CLEAN,
					Constants.Status.ROOM_STATUS_OCCUPY_DIRTY, count);
		}

	}
}
