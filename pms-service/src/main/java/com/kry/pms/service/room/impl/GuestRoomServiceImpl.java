package com.kry.pms.service.room.impl;

import java.time.LocalDateTime;
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
import com.kry.pms.model.http.request.busi.GuestRoomOperation;
import com.kry.pms.model.http.request.busi.RoomLockBo;
import com.kry.pms.model.persistence.room.Floor;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.GuestRoomStatus;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.room.GuestRoomService;
import com.kry.pms.service.room.GuestRoomStatusService;

@Service
public class GuestRoomServiceImpl implements GuestRoomService {
	@Autowired
	GuestRoomDao guestRoomDao;
	@Autowired
	GuestRoomStatusService guestRoomStatusService;
	@Autowired
	RoomRecordService roomRecordService;

	@Override
	public GuestRoom add(GuestRoom guestRoom) {
		guestRoom = guestRoomDao.saveAndFlush(guestRoom);
		guestRoomStatusService.initNewGuestRoomStatus(guestRoom);
		return guestRoom;
	}

	@Override
	public void delete(String id) {
		GuestRoom guestRoom = guestRoomDao.findById(id).get();
		if (guestRoom != null) {
			guestRoom.setDeleted(Constants.DELETED_TRUE);
			guestRoomStatusService.deleteByRoomId(id);
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

	@Override
	public List<GuestRoom> findByFloor(Floor floor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long findCountByFloor(Floor floor) {
		GuestRoom guestRoom = new GuestRoom();
		guestRoom.setFloor(floor);
		Example<GuestRoom> ex = Example.of(guestRoom);
		return guestRoomDao.count(ex);
	}

	@Override
	public DtoResponse<String> locked(RoomLockBo rlb) {
		DtoResponse<String> rep = new DtoResponse<String>();
		String roomIds = rlb.getRoomIds();
		if (roomIds != null && !roomIds.isEmpty()) {
			if (roomIds.contains(Constants.KEY_DEFAULT_SEPARATOR)) {
				if (roomIds.endsWith(Constants.KEY_DEFAULT_SEPARATOR)) {
					roomIds = roomIds.substring(0, roomIds.length() - 2);
				}
				String[] ids = roomIds.split(Constants.KEY_DEFAULT_SEPARATOR);
				if(lockedCheck(ids,rlb.getStartTime(),rlb.getEndTime(),rep)) {
					locked(ids, rlb);
				}
			} else {
				if(lockedCheck(roomIds,rlb.getStartTime(),rlb.getEndTime(), rep)) {
					locked(roomIds, rlb);
				}
			}
		}
		return rep;
	}
	/**
	 * 锁定房间
	 * @param ids
	 * @param rlb
	 * @return
	 */
	private boolean locked(String id, RoomLockBo rlb) {
		GuestRoom gr = findById(id);
		GuestRoomStatus grs = guestRoomStatusService.findGuestRoomStatusByGuestRoom(gr);
		grs.setRoomStatus(Constants.Status.ROOM_STATUS_OUT_OF_SERVCIE);
		return true;
	}
	/**
	 * 锁房确认
	 * @param id
	 * @param rep
	 * @return
	 */
	private boolean lockedCheck(String id,LocalDateTime startTime,LocalDateTime endTime,DtoResponse<String> rep) {
		GuestRoom gr = findById(id);
		boolean result = true;
		if(gr==null) {
			rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			rep.setMessage("没有找到您选择的");
			result = false;
		}else {
			GuestRoomStatus grs = guestRoomStatusService.findGuestRoomStatusByGuestRoom(gr);
			String status = grs.getRoomStatus();
			if(Constants.Status.ROOM_STATUS_OCCUPY_CLEAN.equals(status)||Constants.Status.ROOM_STATUS_OCCUPY_DIRTY.equals(status)) {
				// TODO  还应该考虑锁定时间问题，如果锁定开始时间为有顾客为在住状态，客房将不能被锁定
				rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
				rep.setMessage(grs.getRoomNum()+"  有客人入住无法锁定");
				result = false;
			}
		}
		return result;
	}
	/**
	 * 锁房确认
	 * @param id
	 * @param rep
	 * @return
	 */
	private boolean lockedCheck(String[] ids,LocalDateTime startTime,LocalDateTime endTime,DtoResponse<String> rep) {
		boolean result = true;
		for (String id : ids) {
			if(!lockedCheck(id,startTime,endTime,rep)) {
				result = false;
			}
		}
		return result;
	}
	/**
	 * 锁定房间
	 * @param ids
	 * @param rlb
	 * @return
	 */
	private boolean locked(String[] ids, RoomLockBo rlb) {
		boolean result = true;
		for (String id : ids) {
			if (!locked(id, rlb)) {
				result = false;
				break;
			}
		}
		return result;
	}

	@Override
	public DtoResponse<String> statusOperation(GuestRoomOperation operation) {
		// TODO Auto-generated method stub
		return null;
	}

}
