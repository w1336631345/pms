package com.kry.pms.service.room.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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
import com.kry.pms.dao.room.RoomUsageDao;
import com.kry.pms.model.http.response.room.RoomUsageVo;
import com.kry.pms.model.persistence.busi.BookingItem;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomUsage;
import com.kry.pms.service.busi.BookingItemService;
import com.kry.pms.service.busi.BookingRecordService;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.room.RoomUsageService;

@Service
public class RoomUsageServiceImpl implements RoomUsageService {
	@Autowired
	RoomUsageDao roomUsageDao;
	@Autowired
	BookingRecordService bookingRecordService;
	@Autowired
	CheckInRecordService checkInRecordService;
	@Override
	public RoomUsage add(RoomUsage roomUsage) {
		return roomUsageDao.saveAndFlush(roomUsage);
	}

	@Override
	public void delete(String id) {

		// 特殊处理
		RoomUsage roomUsage = roomUsageDao.findById(id).get();
		roomUsageDao.delete(roomUsage);
	}

	@Override
	public RoomUsage modify(RoomUsage roomUsage) {
		return roomUsageDao.saveAndFlush(roomUsage);
	}

	@Override
	public RoomUsage findById(String id) {
		return roomUsageDao.getOne(id);
	}

	@Override
	public List<RoomUsage> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return roomUsageDao.findByHotelCode(code);
	}
	
	@Override
	public List<RoomUsage> queryUsableGuestRooms(String roomTypeId,LocalDateTime startTime,LocalDateTime endDateTime) {
		return roomUsageDao.queryUsableGuestRooms(roomTypeId, startTime, endDateTime);// 默认不实现
		// return roomUsageDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<RoomUsage> listPage(PageRequest<RoomUsage> prq) {
		Example<RoomUsage> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomUsageDao.findAll(ex, req));
	}

	@Override
	public RoomUsage use(GuestRoom gr, String status, LocalDateTime startTime, LocalDateTime endTime,
			String businesskey, String businessInfo,DtoResponse<String> response) {
		Duration d = Duration.between(startTime, endTime);
		long duration = d.get(ChronoUnit.SECONDS)/3600;
		RoomUsage ru = roomUsageDao.queryGuestRoomUsable(gr.getId(),startTime, endTime);
		RoomUsage data = null;
		if (ru != null) {
			if (ru.getStartDateTime().isEqual(startTime)) {
				if (ru.getEndDateTime().isEqual(endTime)) {
					ru.setEndDateTime(endTime);
					ru.setBusinessInfo(businessInfo);
					ru.setBusinesskey(businesskey);
					ru.setUsageStatus(status);
					data = modify(ru);
				} else {
					RoomUsage npur = new RoomUsage();
					BeanUtils.copyProperties(ru, npur);
					npur.setPostRoomUsage(ru.getPostRoomUsage());
					ru.setEndDateTime(endTime);
					ru.setBusinessInfo(businessInfo);
					ru.setBusinesskey(businesskey);
					ru.setUsageStatus(status);
					ru = modify(ru);
					npur.setPreRoomUsage(ru);
					npur.setId(null);
					updateDuration(npur);
					add(npur);
					ru.setPostRoomUsage(npur);
					data = modify(ru);
				}
			} else {
				if (ru.getEndDateTime().isEqual(endTime)) {
					RoomUsage npur = new RoomUsage();
					BeanUtils.copyProperties(ru, npur);
					ru.setEndDateTime(startTime);
					updateDuration(ru);
					npur.setStartDateTime(endTime);
					npur.setUsageStatus(status);
					npur.setBusinessInfo(businessInfo);
					npur.setId(null);
					npur.setBusinesskey(businesskey);
					ru = modify(ru);
					npur.setPreRoomUsage(ru);
					add(npur);
					ru.setPostRoomUsage(npur);
					modify(ru);
				} else {
					RoomUsage cru = new RoomUsage();
					RoomUsage pru = new RoomUsage();
					BeanUtils.copyProperties(ru, cru);
					BeanUtils.copyProperties(ru, pru);
					cru.setId(null);
					cru.setStartDateTime(startTime);
					cru.setEndDateTime(endTime);
					cru.setBusinessInfo(businessInfo);
					cru.setBusinesskey(businesskey);
					cru.setDuration(duration);
					cru.setUsageStatus(status);
					ru = modify(ru);
					cru.setPreRoomUsage(ru);
					cru = add(cru);
					
					ru.setEndDateTime(startTime);
					updateDuration(ru);
					ru.setPostRoomUsage(cru);
					modify(ru);
					
					pru.setId(null);
					pru.setStartDateTime(endTime);
					pru.setPreRoomUsage(cru);
					updateDuration(pru);
					add(pru);
					cru.setPostRoomUsage(pru);
					data = modify(cru);
				}
			}
		} else {
			response.setStatus(Constants.BusinessCode.CODE_RESOURCE_NOT_ENOUGH);
			response.setMessage("房间号："+gr.getRoomNum()+"在该时段无法办理入住");
		}
		return data;
	}
	@Override
	public RoomUsage use(GuestRoom gr, String status, LocalDateTime startTime, int days,
			String businesskey, String businessInfo,DtoResponse<String> response) {
				return null;
		
	}
	private void updateDuration(RoomUsage ru) {
		long d = Duration.between(ru.getStartDateTime(), ru.getEndDateTime()).get(ChronoUnit.SECONDS);
		ru.setDuration(d/3600);
	}

	@Override
	public List<RoomUsageVo> queryUsableGuestRoomsByBookItemId(String bookItemId) {
		BookingRecord br = bookingRecordService.findByBookingItemId(bookItemId);
		CheckInRecord cir = checkInRecordService.findById(bookItemId);
		List<RoomUsageVo> data = new ArrayList<RoomUsageVo>();
		if(br!=null&&cir!=null) {
			List<RoomUsage> list = queryUsableGuestRooms(cir.getRoomType().getId(), br.getArriveTime(), br.getLeaveTime());
			if(list!=null&&!list.isEmpty()) {
				for(RoomUsage ru:list) {
					data.add(new RoomUsageVo(ru));
				}
				return data;
			}
		}
		return null;
	}

	@Override
	public List<RoomUsageVo> queryUsableGuestRoomsByCheckInRecordId(String cid) {
		CheckInRecord cir = checkInRecordService.findById(cid);
		List<RoomUsageVo> data = new ArrayList<RoomUsageVo>();
		if(cir!=null) {
			List<RoomUsage> list = queryUsableGuestRooms(cir.getRoomType().getId(), cir.getArriveTime(), cir.getLeaveTime());
			if(list!=null&&!list.isEmpty()) {
				for(RoomUsage ru:list) {
					data.add(new RoomUsageVo(ru));
				}
				return data;
			}
		}
		return null;
	}

}
