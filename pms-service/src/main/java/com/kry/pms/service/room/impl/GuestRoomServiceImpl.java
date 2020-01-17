package com.kry.pms.service.room.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.kry.pms.base.*;
import com.kry.pms.dao.room.GuestRoomStatusDao;
import com.kry.pms.dao.room.RoomTypeQuantityDao;
import com.kry.pms.dao.room.RoomUsageDao;
import com.kry.pms.model.persistence.room.*;
import com.kry.pms.service.room.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.dao.room.GuestRoomDao;
import com.kry.pms.model.http.request.busi.GuestRoomOperation;
import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.model.persistence.busi.RoomRepairRecord;
import com.kry.pms.model.persistence.dict.RoomLockReason;
import com.kry.pms.model.persistence.dict.RoomRepairReason;
import com.kry.pms.service.busi.RoomLockRecordService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.busi.RoomRepairRecordService;
import com.kry.pms.service.dict.RoomLockReasonService;
import com.kry.pms.service.dict.RoomRepairReasonService;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
public class GuestRoomServiceImpl implements GuestRoomService {
	@Autowired
	GuestRoomDao guestRoomDao;
	@Autowired
	GuestRoomStatusService guestRoomStatusService;
	@Autowired
	GuestRoomStatusDao guestRoomStatusDao;
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
	@Autowired
	RoomUsageDao roomUsageDao;
	@Autowired
	RoomTypeService roomTypeService;
	@Autowired
	RoomTypeQuantityDao roomTypeQuantityDao;

	public static final String OP_OPEN_REPAIR = "_ROO";
	public static final String OP_OPEN_LOCK = "_ROS";

	@Override
	@Transactional
	public GuestRoom add(GuestRoom guestRoom) {
		guestRoom.setStatus(Constants.Status.NORMAL);
		guestRoom = guestRoomDao.saveAndFlush(guestRoom);
		guestRoomStatusService.initNewGuestRoomStatus(guestRoom);
		return guestRoom;
	}

	@Override
	@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
	public GuestRoom addRoomAndResources(GuestRoom guestRoom){
		try {
			guestRoom.setStatus(Constants.Status.NORMAL);
			guestRoom = guestRoomDao.saveAndFlush(guestRoom);
			//		guestRoomStatusService.initNewGuestRoomStatus(guestRoom);
			addRoomRelated(guestRoom);
		}catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
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
					addRoomAndResources(gr);
					list.add(gr);
				}
			}
			if (rep.getCode() == Constants.CODE_SHOW_LEVEL_ERROR) {
				rep.setMessage(repeatRoomNum.append("房间号重复！！").toString());
			}
		}
		return rep.addData(list);
	}

	@Transactional
	public void addRoomRelated(GuestRoom gr){
		guestRoomStatusService.initNewGuestRoomStatus(gr);
		RoomType rt = roomTypeService.findById(gr.getRoomType().getId());
		rt.setRoomCount(rt.getRoomCount()+1);
		roomTypeService.modify(rt);
		LocalDate date = LocalDate.now();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateStr = date.format(fmt);
		int i = roomTypeQuantityDao.updateAddTotal(gr.getRoomType().getId(), dateStr);
		RoomUsage ru = new RoomUsage();
		ru.setStartDateTime(LocalDateTime.now());
		ru.setEndDateTime(LocalDateTime.now().plus(1, ChronoUnit.YEARS));
		ru.setUsageStatus(Constants.Status.ROOM_USAGE_FREE);
		long d = Duration.between(ru.getStartDateTime(), ru.getEndDateTime()).get(ChronoUnit.SECONDS);
		ru.setDuration(d / 3600);
		ru.setGuestRoom(gr);
		roomUsageDao.saveAndFlush(ru);
	}

	@Transactional
	@Override
	public DtoResponse<GuestRoom> removeRoomRelated(String id){
		GuestRoom gr = findById(id);
		DtoResponse<GuestRoom> rep = new DtoResponse<GuestRoom>();
		boolean isUse = roomUsageService.freeCheck(gr, LocalDateTime.now(), null);
		if(isUse){
			rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			rep.setCode(Constants.CODE_SHOW_LEVEL_ERROR);
			rep.setMessage("房间"+gr.getRoomNum()+"被占用或被预订");
		}else{
			LocalDate date = LocalDate.now();
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String dateStr = date.format(fmt);
			int i = roomTypeQuantityDao.deletedAddTotal(gr.getRoomType().getId(), dateStr);

			guestRoomStatusDao.deleteByGuestRoom(gr);

			RoomType rt = roomTypeService.findById(gr.getRoomType().getId());
			rt.setRoomCount(rt.getRoomCount() - 1);
			roomTypeService.modify(rt);

			roomUsageDao.deleteByGuestRoom(gr);

			guestRoomDao.delete(gr);

		}
		return rep;
	}
	@Transactional
	@Override
	public HttpResponse updateRoom(GuestRoom guestRoom){
		HttpResponse hr = new HttpResponse();
		GuestRoom gr = guestRoomDao.getOne(guestRoom.getId());
		if(!gr.getRoomType().getId().equals(guestRoom.getRoomType().getId())){
			boolean isUse = roomUsageService.freeCheck(gr, LocalDateTime.now(), null);
			if(isUse){
				hr.error("房间"+gr.getRoomNum()+"被占用或被预订");
				return hr;
			}else {
				removeRoomRelated(gr.getId());
				GuestRoom grl = new GuestRoom();
				guestRoom.setId(null);
				BeanUtils.copyProperties(guestRoom, grl);
				grl.setTags(guestRoom.getTags());
				addRoomAndResources(guestRoom);
				hr.setData(grl);
			}
		}else {
			modify(guestRoom);
		}
		return hr;
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
			rep.addData(addRoomAndResources(guestRoom));
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
		LocalDateTime currentDate = LocalDateTime.now();
		int errorCode = 0;
		for (String id : ids) {
			GuestRoom gr = findById(id);
			if (gr != null) {
				switch (op.getOp()) {
				case OP_OPEN_LOCK:
					RoomLockRecord rlr = roomLockRecordService.openLock(id, op.getOperationEmployeeId());
					if (rlr != null && rlr.getStatus().equals(Constants.Status.CLOSE)) {
						roomUsageService.unUse(gr, rlr.getId(), currentDate);
						if (rlr.getStartTime().isBefore(currentDate)) {
							if (guestRoomStatusService.unLockGuestRoom(id, rlr)) {
							} else {
								errorCode = Constants.BusinessCode.CODE_ILLEGAL_OPERATION;
							}
						}
					}
					break;
				case OP_OPEN_REPAIR:
					RoomRepairRecord rrr = roomRepairRecordService.openRepair(id, op.getOperationEmployeeId());
					if (rrr != null && rrr.getStatus().equals(Constants.Status.CLOSE)) {
						roomUsageService.unUse(gr, rrr.getId(), currentDate);
						if (rrr.getStartTime().isBefore(currentDate)) {
							if (guestRoomStatusService.unRepairGuestRoom(id, rrr)) {
							} else {
								errorCode = Constants.BusinessCode.CODE_ILLEGAL_OPERATION;
							}
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
		StringBuilder message = new StringBuilder();
		for (String id : ids) {
			GuestRoom gr = findById(id);
			if (gr != null) {
				switch (op.getOp()) {
				case Constants.Status.ROOM_STATUS_OUT_OF_ORDER:
					if (op.getReasonId() != null && op.getEndToStatus() != null) {
						RoomRepairReason rpr = roomRepairReasonService.findById(op.getReasonId());
						DtoResponse<RoomUsage> r = roomUsageService.use(gr, Constants.Status.ROOM_USAGE_REPARIE,
								op.getStartTime(), op.getEndTime(), null, null);
						if (r.getStatus() == 0) {
							RoomRepairRecord record = roomRepairRecordService.add(roomRepairRecordService
									.createRecord(gr, op.getStartTime(), op.getEndTime(), rpr, op.getEndToStatus()));
							r.getData().setBusinesskey(record.getId());
							roomUsageService.modify(r.getData());
							if (op.getStartTime().toLocalDate().isEqual(LocalDate.now())) {
								guestRoomStatusService.repairGuestRoom(gr.getId(), record);
							}
						} else {
							errorCode = Constants.BusinessCode.CODE_ILLEGAL_OPERATION;
							message.append(gr.getRoomNum());
							message.append(" 房间资源不足，请确认");
							message.append("/n");
						}
					} else {
						rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
						rep.setMessage("必要参数不足：原因或者结束状态未选");
					}
					break;
				case Constants.Status.ROOM_STATUS_OUT_OF_SERVCIE:
					if (op.getReasonId() != null && op.getEndToStatus() != null) {
						RoomLockReason rlr = roomLockReasonService.findById(op.getReasonId());
						DtoResponse<RoomUsage> r = roomUsageService.use(gr, Constants.Status.ROOM_USAGE_LOCKED,
								op.getStartTime(), op.getEndTime(), null, null);
						if (r.getStatus() == 0) {
							RoomLockRecord record = roomLockRecordService.add(roomLockRecordService.createRecord(gr,
									op.getStartTime(), op.getEndTime(), rlr, op.getEndToStatus()));
							r.getData().setBusinesskey(record.getId());
							roomUsageService.modify(r.getData());
							if (op.getStartTime().toLocalDate().isEqual(LocalDate.now())) {
								guestRoomStatusService.lockGuestRoom(gr.getId(), record);
							}
						} else {
							errorCode = Constants.BusinessCode.CODE_ILLEGAL_OPERATION;
							message.append(gr.getRoomNum());
							message.append(" 房间资源不足，请确认");
							message.append("/n");
						}
					} else {
						rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
						rep.setMessage("必要参数不足：原因或者结束状态未选");
					}
					break;
				default:
					if (rep.getStatus() == 0) {
						DtoResponse<String> r = guestRoomStatusService.changeRoomStatus(id, toStatus, 1, false);
						if (r.getStatus() != 0) {
							errorCode = r.getStatus();
							rep.setMessage(rep.getMessage() + r.getMessage());
						}
					}
					break;
				}
			} else {
				// 如果房间找不到 错误请求 不在判断其他房间
				rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
				rep.setMessage("必要参数错误：房间找不到");
				break;
			}
		}
		if (rep.getStatus() == 0) {
			rep.setStatus(errorCode);
			rep.setMessage(message.toString());
		}
		return rep;
	}

}
