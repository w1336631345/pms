package com.kry.pms.service.room.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.room.RoomTypeQuantityDao;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.http.response.busi.RoomTypeQuantityPredictableVo;
import com.kry.pms.model.http.response.room.RoomTypeQuantityVo;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.model.persistence.room.RoomTypeQuantity;
import com.kry.pms.service.room.RoomTypeQuantityService;
import com.kry.pms.service.room.RoomTypeService;

@Service
public class RoomTypeQuantityServiceImpl implements RoomTypeQuantityService {
	@Autowired
	RoomTypeQuantityDao roomTypeQuantityDao;
	@Autowired
	RoomTypeService roomTypeService;

	@Override
	public RoomTypeQuantity add(RoomTypeQuantity roomTypeQuantity) {
		return roomTypeQuantityDao.saveAndFlush(roomTypeQuantity);
	}

	@Override
	public void delete(String id) {
		RoomTypeQuantity roomTypeQuantity = roomTypeQuantityDao.findById(id).get();
		if (roomTypeQuantity != null) {
			roomTypeQuantity.setDeleted(Constants.DELETED_TRUE);
		}
		roomTypeQuantityDao.saveAndFlush(roomTypeQuantity);
	}

	@Override
	public RoomTypeQuantity modify(RoomTypeQuantity roomTypeQuantity) {
		return roomTypeQuantityDao.saveAndFlush(roomTypeQuantity);
	}

	@Override
	public RoomTypeQuantity findById(String id) {
		return roomTypeQuantityDao.getOne(id);
	}

	@Override
	public List<RoomTypeQuantity> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return roomTypeQuantityDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<RoomTypeQuantity> listPage(PageRequest<RoomTypeQuantity> prq) {
		Example<RoomTypeQuantity> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomTypeQuantityDao.findAll(ex, req));
	}

	@Override
	public List<RoomTypeQuantity> updateQuantitys(List<RoomTypeQuantity> data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RoomTypeQuantity> findByQuantityDate(RoomType roomType, LocalDate startDate, int days) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RoomTypeQuantity> findQuantitysForUpdate(RoomType roomType, LocalDate startDate, LocalDate endDate) {
		return roomTypeQuantityDao.findQuantitysForUpdate(roomType, startDate, endDate);
	}

	@Override
	public RoomTypeQuantity findByRoomTypeAndQuantityDateForUpdate(RoomType roomType, LocalDate quantityDate) {
		RoomTypeQuantity roomTypeQuantity = roomTypeQuantityDao.findByRoomTypeAndQuantityDate(roomType, quantityDate);
		if (roomTypeQuantity == null) {
			roomTypeQuantity = initRoomTypeQuantity(roomType, quantityDate);
			roomTypeQuantity = add(roomTypeQuantity);
		}
		return roomTypeQuantity;
	}

	private RoomTypeQuantity initRoomTypeQuantity(RoomType roomType, LocalDate quantityDate) {
		RoomTypeQuantity rtq = new RoomTypeQuantity();
		rtq.setRoomType(roomType);
		rtq.setRoomTypeCode(roomType.getCode());
		rtq.setRoomTypeName(roomType.getName());
		rtq.setQuantityDate(quantityDate);
		rtq.setAvailableTotal(roomType.getOverReservation());
		rtq.setPredictableTotal(roomType.getOverReservation());
		rtq.setRoomCount(roomType.getRoomCount());
		return rtq;
	}

	@Override
	public boolean bookCheck(RoomType roomType, LocalDate quantityDate, int quantity) {
		RoomTypeQuantity rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, quantityDate);
		if (rtq.getPredictableTotal() < quantity) {
			return false;
		} else {
			rtq.setBookingTotal(rtq.getBookingTotal() + quantity);
			rtq.setPredictableTotal(rtq.getPredictableTotal() - quantity);
			modify(rtq);
			return true;
		}
	}

	public void useRoomType(RoomType roomType, LocalDateTime startTime, LocalDateTime endTime, String useType) {
		LocalDate startDate = startTime.toLocalDate();
		LocalDate endDate = endTime.toLocalDate();
		useRoomType(roomType, startDate, endDate, useType);
	}

	@Transactional
	@Override
	public void useRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, String useType) {
		useRoomType(roomType, startDate, endDate, useType, 1);

	}

	public void useRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, String useType, int quantity) {
		switch (useType) {
		case Constants.Status.ROOM_USAGE_BOOK:
			bookRoomType(roomType, startDate, endDate, quantity);
			break;
		case Constants.Status.ROOM_USAGE_ASSIGN:
			assignRoomType(roomType, startDate, endDate, quantity);
			break;
		case Constants.Status.ROOM_USAGE_CHECK_IN:
			checkInRoomType(roomType, startDate, endDate, quantity);
			break;
		case Constants.Status.ROOM_USAGE_LOCKED:
			lockRoomType(roomType, startDate, endDate, quantity);
			break;
		case Constants.Status.ROOM_USAGE_REPARIE:
			repairRoomType(roomType, startDate, endDate, quantity);
			break;
		default:
			break;
		}
	}

	@Override
	public void unUseRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, String useType) {
		unUseRoomType(roomType, startDate, endDate, useType, 1);
	}

	public void unUseRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, String useType, int quantity) {
		switch (useType) {
		case Constants.Status.ROOM_USAGE_BOOK:
			bookRoomType(roomType, startDate, endDate, quantity);
			break;
		case Constants.Status.ROOM_USAGE_ASSIGN:
			assignRoomType(roomType, startDate, endDate, quantity);
			break;
		case Constants.Status.ROOM_USAGE_CHECK_IN:
			unCheckInRoomType(roomType, startDate, endDate, quantity);
			break;
		case Constants.Status.ROOM_USAGE_LOCKED:
			lockRoomType(roomType, startDate, endDate, quantity);
			break;
		case Constants.Status.ROOM_USAGE_REPARIE:
			repairRoomType(roomType, startDate, endDate, quantity);
			break;
		default:
			break;
		}
	}

	@Override
	public void changeRoomTypeQuantity(RoomType roomType, LocalDate startDate, LocalDate endDate, String oldUsageStatus,
			String newUsageStatus, int quantity) {
		RoomTypeQuantity rtq = null;
		LocalDate currentDate = startDate;
		while (currentDate.isBefore(endDate)) {
			rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
			plusQuantity(rtq, oldUsageStatus, -quantity);
			plusQuantity(rtq, newUsageStatus, quantity);
			currentDate = currentDate.plusDays(1);
			modify(rtq);
		}

	}

	private void plusQuantity(RoomTypeQuantity rtq, String status, int quantity) {
		switch (status) {
		case Constants.Status.ROOM_USAGE_CHECK_IN:
			rtq.setUsedTotal(rtq.getUsedTotal() + quantity);
			break;
		case Constants.Status.ROOM_USAGE_ASSIGN:
			rtq.setBookingTotal(rtq.getBookingTotal() + quantity);
			break;
		case Constants.Status.ROOM_USAGE_BOOK:
			rtq.setReserveTotal(rtq.getReserveTotal() + quantity);
			break;
		case Constants.Status.ROOM_USAGE_FREE:
			rtq.setAvailableTotal(rtq.getAvailableTotal() + quantity);
			break;
		case Constants.Status.ROOM_USAGE_CHECK_OUT:
			rtq.setPredictableTotal(rtq.getPredictableTotal() + quantity);
			break;
		case Constants.Status.ROOM_USAGE_LOCKED:
			rtq.setLockedTotal(rtq.getLockedTotal() + quantity);
			break;
		case Constants.Status.ROOM_USAGE_REPARIE:
			rtq.setRepairTotal(rtq.getRepairTotal() + quantity);
			break;
		default:
			break;
		}

	}

	private void unCheckInRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
		RoomTypeQuantity rtq = null;
		LocalDate currentDate = startDate;
		while (currentDate.isBefore(endDate)) {
			rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
			rtq.setBookingTotal(rtq.getBookingTotal() + quantity);
			rtq.setUsedTotal(rtq.getUsedTotal() - quantity);
			currentDate = currentDate.plusDays(1);
			modify(rtq);
		}
	}

	private void bookRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
		RoomTypeQuantity rtq = null;
		LocalDate currentDate = startDate;
		while (currentDate.isBefore(endDate)) {
			rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
			rtq.setPredictableTotal(rtq.getPredictableTotal() - quantity);
			rtq.setReserveTotal(rtq.getReserveTotal() + quantity);
			currentDate = currentDate.plusDays(1);
			modify(rtq);
		}
	}

	private void assignRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
		RoomTypeQuantity rtq = null;
		LocalDate currentDate = startDate;
		while (currentDate.isBefore(endDate)) {
			rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
			rtq.setReserveTotal(rtq.getReserveTotal() - quantity);
			rtq.setBookingTotal(rtq.getBookingTotal() + quantity);
			currentDate = currentDate.plusDays(1);
			modify(rtq);
		}
	}

	private void lockRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
		RoomTypeQuantity rtq = null;
		LocalDate currentDate = startDate;
		while (currentDate.isBefore(endDate)) {
			rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
			rtq.setAvailableTotal(rtq.getAvailableTotal() - quantity);
			rtq.setLockedTotal(rtq.getLockedTotal() + quantity);
			currentDate = currentDate.plusDays(1);
			modify(rtq);
		}
	}

	private void repairRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
		RoomTypeQuantity rtq = null;
		LocalDate currentDate = startDate;
		while (currentDate.isBefore(endDate)) {
			rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
			rtq.setAvailableTotal(rtq.getAvailableTotal() - quantity);
			rtq.setRepairTotal(rtq.getRepairTotal() + quantity);
			currentDate = currentDate.plusDays(1);
			modify(rtq);
		}
	}

	private void checkInRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
		RoomTypeQuantity rtq = null;
		LocalDate currentDate = startDate;
		while (currentDate.isBefore(endDate)) {
			rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
			rtq.setBookingTotal(rtq.getBookingTotal() - quantity);
			rtq.setUsedTotal(rtq.getUsedTotal() + quantity);
			currentDate = currentDate.plusDays(1);
			modify(rtq);
		}
	}

	@Override
	public void checkInRoomTypeWithoutBook(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
		RoomTypeQuantity rtq = null;
		LocalDate currentDate = startDate;
		while (currentDate.isBefore(endDate)) {
			rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
			rtq.setPredictableTotal(rtq.getPredictableTotal() - quantity);
			rtq.setUsedTotal(rtq.getUsedTotal() + quantity);
			currentDate = currentDate.plusDays(1);
			modify(rtq);
		}
	}

	public void unUseRoomType(RoomType roomType, LocalDateTime startTime, LocalDateTime endTime) {
		LocalDate startDate = startTime.toLocalDate();
		LocalDate endDate = endTime.toLocalDate();
	}

	public void unUseRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {

	}

	@Override
	public void checkIn(GuestRoom gr, LocalDate startDate, Integer days) {
		RoomTypeQuantity rtq = null;
		for (int i = 0; i < days; i++) {
			rtq = findByRoomTypeAndQuantityDateForUpdate(gr.getRoomType(), startDate.plusDays(i));
			rtq.setUsedTotal(rtq.getUsedTotal() + 1);
			rtq.setPredictableTotal(rtq.getPredictableTotal() - 1);
			modify(rtq);
		}
		rtq = findByRoomTypeAndQuantityDateForUpdate(gr.getRoomType(), startDate.plusDays(days));
		rtq.setWillLeaveTotal(rtq.getWillLeaveTotal() + 1);
		modify(rtq);
	}

	@Override
	public List<RoomTypeQuantityPredictableVo> queryPredictable(String hotelCode, LocalDate startDate,
			LocalDate endDate) {
		List<RoomType> types = roomTypeService.getAllByHotelCode(hotelCode, Constants.DELETED_FALSE);
		List<RoomTypeQuantityPredictableVo> data = new ArrayList<>();
		RoomTypeQuantityPredictableVo rtpv = null;
		for (RoomType type : types) {
			RoomTypeQuantity rtq = roomTypeQuantityDao.queryPredictable(type.getId(), startDate, endDate);
			if (rtq != null) {
				rtpv = new RoomTypeQuantityPredictableVo();
				rtpv.setPrice(type.getPrice());
				rtpv.setRoomTypeName(type.getName());
				rtpv.setRoomTypeId(type.getId());
				rtpv.setStartDate(startDate);
				rtpv.setEndDate(endDate);
				rtpv.setAvailableTotal(rtq.getPredictableTotal());
				data.add(rtpv);
			}
		}
		return data;
	}

	@Override
	public RoomTypeQuantityPredictableVo queryPredic(String currentHotleCode, String roomTypeId, LocalDate startDate,
			LocalDate endDate) {
		RoomType roomType = roomTypeService.findById(roomTypeId);
		RoomTypeQuantityPredictableVo rtpv = null;
		if (roomType != null) {
			RoomTypeQuantity rtq = roomTypeQuantityDao.queryPredictable(roomTypeId, startDate, endDate);
			if (rtq != null) {
				rtpv = new RoomTypeQuantityPredictableVo();
				rtpv.setPrice(roomType.getPrice());
				rtpv.setRoomTypeName(roomType.getName());
				rtpv.setRoomTypeId(roomType.getId());
				rtpv.setStartDate(startDate);
				rtpv.setEndDate(endDate);
				rtpv.setAvailableTotal(rtq.getPredictableTotal());
			}
		}
		return rtpv;
	}

	@Override
	public List<RoomTypeQuantityVo> queryByDay(String currentHotleCode, LocalDate startDate, LocalDate endDate) {
		List<RoomTypeQuantityVo> data = new ArrayList<RoomTypeQuantityVo>();
		RoomTypeQuantityVo rv = null;
		for (RoomTypeQuantity r : roomTypeQuantityDao.queryByDay(currentHotleCode, startDate, endDate)) {
			rv = new RoomTypeQuantityVo();
			BeanUtils.copyProperties(r, rv);
			rv.setRoomTypeId(r.getRoomType().getId());
			data.add(rv);
		}
		return data;
	}

	@Override
	public void useRoomType(UseInfoAble info, String userType) {
		useRoomType(info.roomType(), info.getStartTime().toLocalDate(), info.getEndTime().toLocalDate(), userType,
				info.getRoomCount());
	}

	@Override
	public List<RoomTypeQuantityVo> queryOneDay(String currentHotleCode, LocalDate date) {
		List<RoomTypeQuantityVo> data = new ArrayList<RoomTypeQuantityVo>();
		RoomTypeQuantityVo rv = null;
		for (RoomTypeQuantity r : roomTypeQuantityDao.findByHotelCodeAndQuantityDate(currentHotleCode, date)) {
			rv = new RoomTypeQuantityVo();
			BeanUtils.copyProperties(r, rv);
			rv.setRoomTypeId(r.getRoomType().getId());
			data.add(rv);
		}
		return data;
	}
}
