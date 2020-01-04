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
import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.model.persistence.busi.RoomRepairRecord;
import com.kry.pms.model.persistence.dict.RoomLockReason;
import com.kry.pms.model.persistence.dict.RoomRepairReason;
import com.kry.pms.model.persistence.room.Floor;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.busi.RoomLockRecordService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.busi.RoomRepairRecordService;
import com.kry.pms.service.dict.RoomLockReasonService;
import com.kry.pms.service.dict.RoomRepairReasonService;
import com.kry.pms.service.room.GuestRoomService;
import com.kry.pms.service.room.GuestRoomStatusService;
import com.kry.pms.service.room.RoomUsageService;

@Service
public class GuestRoomServiceImpl implements GuestRoomService {
	@Autowired
	GuestRoomDao guestRoomDao;
	@Autowired
	GuestRoomStatusService guestRoomStatusService;
	@Autowired
	RoomRecordService roomRecordService;
	@Autowired
	RoomLockRecordService roomLockRecordService;
	@Autowired
	RoomRepairReasonService roomRepairReasonService;
	@Autowired
	RoomRepairRecordService roomRepairRecordService;
	@Autowired
	RoomLockReasonService roomLockReasonService;
	@Autowired
	RoomUsageService roomUsageService;

	public static final String OP_OPEN_REPAIR = "_ROO";
	public static final String OP_OPEN_LOCK = "_ROS";

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

	private DtoResponse<String> openOperation(GuestRoomOperation op) {
		DtoResponse<String> rep = new DtoResponse<>();
		rep.setMessage("");
		String[] ids = op.getRoomIds();
		int errorCode = 0;
		for (String id : ids) {
			GuestRoom gr = findById(id);
			if (gr != null) {
				switch (op.getOp()) {
				case OP_OPEN_LOCK:
					RoomLockRecord rlr = roomLockRecordService.openLock(id, op.getOperationEmployeeId());
					if (rlr != null && rlr.getStatus().equals(Constants.Status.CLOSE)) {
						DtoResponse<String> r = guestRoomStatusService.changeRoomStatus(id, rlr.getEndToStatus(), 1,true);
						if (r.getStatus() != 0) {
							errorCode = r.getStatus();
							rep.setMessage(rep.getMessage() + r.getMessage());
						}else {							
							roomUsageService.unUse(gr, rlr.getId(), LocalDateTime.now());
						}
					}
					break;
				case OP_OPEN_REPAIR:
					RoomRepairRecord rrr = roomRepairRecordService.openRepair(id, op.getOperationEmployeeId());
					if (rrr != null && rrr.getStatus().equals(Constants.Status.CLOSE)) {
						DtoResponse<String> r = guestRoomStatusService.changeRoomStatus(id, rrr.getEndToStatus(), 1,true);
						
						if (r.getStatus() != 0) {
							errorCode = r.getStatus();
							rep.setMessage(rep.getMessage() + r.getMessage());
						}else {
							roomUsageService.unUse(gr, rrr.getId(), LocalDateTime.now());
						}
					}
					break;
				}
			} else {
				// 如果房间找不到，错误请求，不在判断其他房间
				rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
				rep.setMessage("必要参数错误：房间找不到");
				break;
			}
			if (rep.getStatus() == 0) {
				rep.setStatus(errorCode);
			}
		}
		return rep;
	}

	@Transactional
	@Override
	public DtoResponse<String> statusOperation(GuestRoomOperation op) {
		if (op.getOp().startsWith("_R")) {
			return openOperation(op);
		}
		DtoResponse<String> rep = new DtoResponse<>();
		rep.setMessage("");
		String[] ids = op.getRoomIds();
		String toStatus = op.getOp();
		int errorCode = 0;
		for (String id : ids) {
			GuestRoom gr = findById(id);
			if (gr != null) {
				switch (op.getOp()) {
				case Constants.Status.ROOM_STATUS_OUT_OF_ORDER:
					if (op.getReasonId() != null && op.getEndToStatus() != null) {
						RoomRepairReason rpr = roomRepairReasonService.findById(op.getReasonId());
						RoomRepairRecord record = roomRepairRecordService.add(roomRepairRecordService.createRecord(gr, op.getStartTime(),
								op.getEndTime(), rpr, op.getEndToStatus()));
						roomUsageService.use(gr, Constants.Status.ROOM_USAGE_LOCKED, op.getStartTime(), op.getEndTime(), record.getId(), null, rep);

					} else {
						rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
						rep.setMessage("必要参数不足：原因或者结束状态未选");
					}
					break;
				case Constants.Status.ROOM_STATUS_OUT_OF_SERVCIE:
					if (op.getReasonId() != null && op.getEndToStatus() != null) {
						RoomLockReason rlr = roomLockReasonService.findById(op.getReasonId());
						RoomLockRecord record = roomLockRecordService.add(roomLockRecordService.createRecord(gr, op.getStartTime(),
								op.getEndTime(), rlr, op.getEndToStatus()));
						roomUsageService.use(gr, Constants.Status.ROOM_USAGE_LOCKED, op.getStartTime(), op.getEndTime(), record.getId(), null, rep);
					} else {
						rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
						rep.setMessage("必要参数不足：原因或者结束状态未选");
					}
					break;
				default:
					break;
				}
				if (rep.getStatus() == 0) {
					DtoResponse<String> r = guestRoomStatusService.changeRoomStatus(id, toStatus, 1,false);
					if (r.getStatus() != 0) {
						errorCode = r.getStatus();
						rep.setMessage(rep.getMessage() + r.getMessage());
					}
				}
			} else {
				// 如果房间找不到，错误请求，不在判断其他房间
				rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
				rep.setMessage("必要参数错误：房间找不到");
				break;
			}
		}
		if (rep.getStatus() == 0) {
			rep.setStatus(errorCode);
		}
		return rep;
	}

}
