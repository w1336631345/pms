package com.kry.pms.service.room.impl;

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
import com.kry.pms.dao.room.GuestRoomStatusDao;
import com.kry.pms.dao.room.RoomStatusQuantityDao;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.GuestRoomStatus;
import com.kry.pms.service.room.GuestRoomStatusService;
import com.kry.pms.service.room.RoomStatusQuantityService;

@Service
public class GuestRoomStatusServiceImpl implements GuestRoomStatusService {
	@Autowired
	GuestRoomStatusDao guestRoomStatusDao;
	@Autowired
	RoomStatusQuantityService roomStatusQuantityService;

	@Override
	public GuestRoomStatus add(GuestRoomStatus guestRoomStatus) {
		return guestRoomStatusDao.saveAndFlush(guestRoomStatus);
	}

	@Override
	public void delete(String id) {
		GuestRoomStatus guestRoomStatus = guestRoomStatusDao.findById(id).get();
		if (guestRoomStatus != null) {
			guestRoomStatus.setDeleted(Constants.DELETED_TRUE);
		}
		modify(guestRoomStatus);
	}

	@Override
	public GuestRoomStatus modify(GuestRoomStatus guestRoomStatus) {
		return guestRoomStatusDao.saveAndFlush(guestRoomStatus);
	}

	@Override
	public GuestRoomStatus findById(String id) {
		return guestRoomStatusDao.getOne(id);
	}

	@Override
	public List<GuestRoomStatus> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return guestRoomStatusDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<GuestRoomStatus> listPage(PageRequest<GuestRoomStatus> prq) {
		Example<GuestRoomStatus> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(guestRoomStatusDao.findAll(ex, req));
	}

	@Override
	public void checkIn(GuestRoom guestRoom, LocalDate checkInDate, String summary, boolean group, boolean linked,
			boolean hourRoom, boolean free, boolean overdued) {
		GuestRoomStatus grs = new GuestRoomStatus();
		grs.setGuestRoom(guestRoom);
		Example<GuestRoomStatus> ex = Example.of(grs);
		grs = guestRoomStatusDao.findOne(ex).orElse(null);
		if (grs == null) {
			grs = initGuestStatusRoom(guestRoom);
		}
		grs.setSummary(summary);
		grs.setGroup(group);
		grs.setHourRoom(hourRoom);
		grs.setFree(free);
		grs.setOverdued(overdued);
		grs.setLinkedRoom(linked);
		if (Constants.Status.ROOM_STATUS_VACANT_CLEAN.equals(grs.getRoomStatus())) {
			roomStatusQuantityService.transformRoomStatusQuantity(guestRoom.getHotelCode(), grs.getRoomStatus(),
					Constants.Status.ROOM_STATUS_OCCUPY_CLEAN, 1);
			grs.setRoomStatus(Constants.Status.ROOM_STATUS_OCCUPY_CLEAN);
		} else {
			roomStatusQuantityService.transformRoomStatusQuantity(guestRoom.getHotelCode(), grs.getRoomStatus(),
					Constants.Status.ROOM_STATUS_OCCUPY_DIRTY, 1);
			grs.setRoomStatus(Constants.Status.ROOM_STATUS_OCCUPY_DIRTY);
		}
		modify(grs);

	}

	private GuestRoomStatus initGuestStatusRoom(GuestRoom guestRoom) {
		GuestRoomStatus grs = new GuestRoomStatus();
		grs.setGuestRoom(guestRoom);
		grs.setRoomNum(guestRoom.getRoomNum());
		grs.setHotelCode(guestRoom.getHotelCode());
		grs.setRoomStatus(Constants.Status.ROOM_STATUS_VACANT_CLEAN);
		grs.setRoomTypeName(guestRoom.getRoomType().getName());
		return grs;
	}

	@Override
	public GuestRoomStatus findGuestRoomStatusByGuestRoom(GuestRoom gr) {
		return guestRoomStatusDao.findByGuestRoomId(gr.getId());
//		GuestRoomStatus grs = new GuestRoomStatus();
//		grs.setGuestRoom(gr);
//		Example<GuestRoomStatus> ex = Example.of(grs);
//		return guestRoomStatusDao.findOne(ex).orElse(null);
	}

	@Override
	public void dailyVerify(GuestRoom guestRoom) {
		GuestRoomStatus grs = findGuestRoomStatusByGuestRoom(guestRoom);
		grs.setRoomStatus(Constants.Status.ROOM_STATUS_OCCUPY_DIRTY);
		modify(grs);
	}

	@Override
	public int batchChangeRoomStatus(String hotelCode, String currentRoomStatus, String toRoomStatus) {
		return guestRoomStatusDao.batchChangeRoomStatus(hotelCode, currentRoomStatus, toRoomStatus);
	}

	@Override
	public void checkOut(String roomId) {
		// TODO Auto-generated method stub
		
	}

}
