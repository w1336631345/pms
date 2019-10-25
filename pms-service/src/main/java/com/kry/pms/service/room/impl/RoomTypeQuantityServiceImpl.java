package com.kry.pms.service.room.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.room.RoomTypeQuantityDao;
import com.kry.pms.model.http.response.busi.RoomTypeQuantityPredictableVo;
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

	@Override
	public void checkIn(GuestRoom gr, LocalDate startDate, Integer days) {
		RoomTypeQuantity rtq = null;
		for (int i = 0; i < days; i++) {
			rtq = findByRoomTypeAndQuantityDateForUpdate(gr.getRoomType(), startDate.plusDays(i));
			rtq.setUsedTotal(rtq.getUsedTotal()+1);
			rtq.setPredictableTotal(rtq.getPredictableTotal()-1);
			modify(rtq);
		}
		rtq = findByRoomTypeAndQuantityDateForUpdate(gr.getRoomType(), startDate.plusDays(days));
		rtq.setWillLeaveTotal(rtq.getWillLeaveTotal()+1);
		modify(rtq);
	}

	@Override
	public List<RoomTypeQuantityPredictableVo> queryPredictable(String hotelCode, LocalDate startDate,
			LocalDate endDate) {
		List<RoomType> types = roomTypeService.getAllByHotelCode(hotelCode, Constants.DELETED_FALSE);
		List<RoomTypeQuantityPredictableVo> data = new ArrayList<>();
		RoomTypeQuantityPredictableVo rtpv= null;
		for(RoomType type:types) {
			RoomTypeQuantity rtq = roomTypeQuantityDao.queryPredictable(type.getId(), startDate,endDate);
			if(rtq!=null) {
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
}
