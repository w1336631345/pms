﻿package com.kry.pms.service.room.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

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
import com.kry.pms.dao.room.GuestRoomDao;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.room.GuestRoomService;

@Service
public class GuestRoomServiceImpl implements GuestRoomService {
	@Autowired
	GuestRoomDao guestRoomDao;

	@Override
	public GuestRoom add(GuestRoom guestRoom) {
		return guestRoomDao.saveAndFlush(guestRoom);
	}

	@Override
	public void delete(String id) {
		GuestRoom guestRoom = guestRoomDao.findById(id).get();
		if (guestRoom != null) {
			guestRoom.setDeleted(Constants.DELETED_TRUE);
		}
		guestRoomDao.saveAndFlush(guestRoom);
	}

	@Override
	public GuestRoom modify(GuestRoom guestRoom) {
		return guestRoomDao.saveAndFlush(guestRoom);
	}

	@Override
	public GuestRoom findById(String id) {
		return guestRoomDao.findById(id).orElse(null);
	}

	@Override
	public List<GuestRoom> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return guestRoomDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<GuestRoom> listPage(PageRequest<GuestRoom> prq) {
		Example<GuestRoom> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(guestRoomDao.findAll(ex, req));
	}

	@Transactional
	@Override
	public DtoResponse<List<GuestRoom>> batchAdd(GuestRoom guestRoom) {
		DtoResponse<List<GuestRoom>> rep = new DtoResponse<List<GuestRoom>>();
		String roomNums = guestRoom.getRoomNum();
		List<GuestRoom> list = new ArrayList<GuestRoom>();
		StringBuilder repeatRoomNum = new StringBuilder();
		if (roomNums.contains(",")) {
			String[] rns = roomNums.split(",");
			for (String rn : rns) {
				GuestRoom gr = new GuestRoom();
				BeanUtils.copyProperties(guestRoom, gr);
				GuestRoom ogr = guestRoomDao.findByHotelCodeAndRoomNumAndDeleted(gr.getHotelCode(), rn,
						Constants.DELETED_FALSE);
				if (ogr != null) {
					rep.setCode(Constants.CODE_SHOW_LEVEL_ERROR);
					repeatRoomNum.append(" ");
					repeatRoomNum.append(rn);
					continue;
				} else {
					gr.setRoomNum(rn);
					add(gr);
					list.add(gr);
				}
			}
			if (rep.getCode() == Constants.CODE_SHOW_LEVEL_ERROR) {
				rep.setMessage(repeatRoomNum.append("房间号重复！！").toString());
			}
		}
		return rep.addData(list);
	}

	@Override
	public DtoResponse<GuestRoom> addWithDto(GuestRoom guestRoom) {
		DtoResponse<GuestRoom> rep = new DtoResponse<GuestRoom>();
		GuestRoom ogr = guestRoomDao.findByHotelCodeAndRoomNumAndDeleted(guestRoom.getHotelCode(),
				guestRoom.getRoomNum(), Constants.DELETED_FALSE);
		if (ogr != null) {
			rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			rep.setCode(Constants.CODE_SHOW_LEVEL_ERROR);
			rep.setMessage("房间编号重复：" + guestRoom.getRoomNum());
		} else {
			rep.addData(add(guestRoom));
		}
		return rep;
	}

}
