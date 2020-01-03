package com.kry.pms.service.room.impl;

import java.time.LocalDate;
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
import com.kry.pms.dao.room.GuestRoomStatusDao;
import com.kry.pms.model.http.response.room.BuildingVo;
import com.kry.pms.model.http.response.room.FloorVo;
import com.kry.pms.model.http.response.room.GuestRoomStatusVo;
import com.kry.pms.model.http.response.room.RoomStatusTableVo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.room.Building;
import com.kry.pms.model.persistence.room.Floor;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.GuestRoomStatus;
import com.kry.pms.service.room.BuildingService;
import com.kry.pms.service.room.FloorService;
import com.kry.pms.service.room.GuestRoomService;
import com.kry.pms.service.room.GuestRoomStatusService;
import com.kry.pms.service.room.RoomStatusQuantityService;
import com.kry.pms.service.room.RoomTypeService;

@Service
public class GuestRoomStatusServiceImpl implements GuestRoomStatusService {
	@Autowired
	GuestRoomStatusDao guestRoomStatusDao;
	@Autowired
	RoomStatusQuantityService roomStatusQuantityService;
	@Autowired
	RoomTypeService roomTypeService;
	@Autowired
	BuildingService buildingService;
	@Autowired
	GuestRoomService guestRoomService;
	@Autowired
	FloorService floorService;

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
	public GuestRoomStatus initNewGuestRoomStatus(GuestRoom guestRoom) {
		GuestRoomStatus status = new GuestRoomStatus();
		if (guestRoom.getRoomType().getName() == null) {
			guestRoom.setRoomType(roomTypeService.findById(guestRoom.getRoomType().getId()));
		}
		status.setRoomTypeName(guestRoom.getRoomType().getName());
		status.setGuestRoom(guestRoom);
		status.setRoomNum(guestRoom.getRoomNum());
		initStatus(status);
		status = add(status);
		return status;
	}

	private void initStatus(GuestRoomStatus status) {
		status.setRoomStatus(Constants.Status.ROOM_STATUS_VACANT_CLEAN);
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

	@Override
	public void checkIn(CheckInRecord cir) {
		GuestRoomStatus grs = findGuestRoomStatusByGuestRoom(cir.getGuestRoom());
		if (grs != null) {
			grs.setRoomStatus(Constants.Status.ROOM_STATUS_OCCUPY_CLEAN);
			String summary = grs.getSummary();
			if (summary == null) {
				summary = cir.getCustomer().getName();
			} else {
				summary += cir.getCustomer().getName();
			}
			grs.setSummary(summary);
			List<CheckInRecord> crs = grs.getCurrentCheckInRecords();
			if (crs != null) {
				grs.getCurrentCheckInRecords().add(cir);
			} else {
				crs = new ArrayList<>();
				crs.add(cir);
			}
			modify(grs);
		}
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
		GuestRoomStatus grs = guestRoomStatusDao.findByGuestRoomId(roomId);
		if (grs != null) {
			grs.setRoomNum(Constants.Status.ROOM_STATUS_VACANT_DIRTY);
			grs.setCurrentCheckInRecords(null);
			modify(grs);
		}
	}

	@Override
	public void linkedRoom(String roomId, boolean status) {
		GuestRoomStatus grs = guestRoomStatusDao.findByGuestRoomId(roomId);
		if (grs != null) {
			grs.setLinkedRoom(status);
			modify(grs);
		}
	}

	@Override
	public void addTogether(String roomId, CheckInRecord checkInRecord) {
		GuestRoomStatus grs = guestRoomStatusDao.findByGuestRoomId(roomId);
		if (grs != null) {
			grs.setSummary(grs.getSummary() + checkInRecord.getGuestRoom());
			grs.getCurrentCheckInRecords().add(checkInRecord);
			modify(grs);
		}
	}

	@Override
	public RoomStatusTableVo table(String currentHotleCode) {
		RoomStatusTableVo roomStatusTableVo = new RoomStatusTableVo();
		ArrayList<BuildingVo> data = new ArrayList<>();
		List<Building> list = buildingService.getAllByHotelCode(currentHotleCode);
		ArrayList<FloorVo> fvs = null;
		ArrayList<GuestRoomStatusVo> grsvs = null;
		GuestRoomStatusVo grsv = null;
		FloorVo fv = null;
		BuildingVo bv = null;
		for (Building b : list) {
			List<Floor> fs = floorService.findByBuildingId(b.getId(), Constants.DELETED_FALSE);
			if (fs != null && !fs.isEmpty()) {
				fvs = new ArrayList<FloorVo>();
				for (Floor f : fs) {
					List<GuestRoomStatus> rs = guestRoomStatusDao.queryByFloorId(f.getId());
					if (rs != null && !rs.isEmpty()) {
						grsvs = new ArrayList<>();
						for (GuestRoomStatus grs : rs) {
							grsv = new GuestRoomStatusVo();
							BeanUtils.copyProperties(grs, grsv);
							grsv.setRoomStatusId(grs.getId());
							grsv.setRoomNum(grs.getGuestRoom().getRoomNum());
							grsv.setGuestRoomId(grs.getGuestRoom().getId());
							grsvs.add(grsv);
						}
						fv = new FloorVo();
						BeanUtils.copyProperties(f, fv);
						fv.setRooms(grsvs);
						fvs.add(fv);
					}

				}
				bv = new BuildingVo();
				BeanUtils.copyProperties(b, bv);
				bv.setFloors(fvs);
				data.add(bv);
			}
		}
		roomStatusTableVo.setBuildings(data);
		return roomStatusTableVo;
	}

	public boolean statusChangeSure(String oldStatus, String newStatus) {
		switch (newStatus) {
		case Constants.Status.ROOM_STATUS_OCCUPY_CLEAN:
			if (oldStatus.equals(Constants.Status.ROOM_STATUS_OCCUPY_DIRTY)) {
				return true;
			}
		case Constants.Status.ROOM_STATUS_OCCUPY_DIRTY:
			if (oldStatus.equals(Constants.Status.ROOM_STATUS_OCCUPY_CLEAN)) {
				return true;
			}
		case Constants.Status.ROOM_STATUS_VACANT_CLEAN:
			if (oldStatus.equals(Constants.Status.ROOM_STATUS_VACANT_DIRTY)) {
				return true;
			}
		case Constants.Status.ROOM_STATUS_VACANT_DIRTY:
			if (oldStatus.equals(Constants.Status.ROOM_STATUS_VACANT_CLEAN)) {
				return true;
			}
		case Constants.Status.ROOM_STATUS_OUT_OF_ORDER:
			if (oldStatus.equals(Constants.Status.ROOM_STATUS_VACANT_DIRTY)
					|| oldStatus.equals(Constants.Status.ROOM_STATUS_VACANT_CLEAN)) {
				return true;
			}
		case Constants.Status.ROOM_STATUS_OUT_OF_SERVCIE:
			if (oldStatus.equals(Constants.Status.ROOM_STATUS_VACANT_DIRTY)
					|| oldStatus.equals(Constants.Status.ROOM_STATUS_VACANT_CLEAN)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public DtoResponse<String> changeRoomStatus(String id, String status, int quantity,boolean force) {
		DtoResponse<String> rep = new DtoResponse<String>();
		GuestRoomStatus roomStatus = guestRoomStatusDao.findByGuestRoomId(id);
		String oldStatus = roomStatus.getRoomStatus();
		if (force||statusChangeSure(oldStatus, status)) {
			roomStatus.setRoomStatus(status);
			modify(roomStatus);
			roomStatusQuantityService.transformRoomStatusQuantity(roomStatus.getHotelCode(), oldStatus, status, 1);
		} else {
			rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
			rep.setMessage(roomStatus.getRoomNum() + ":当前状态为：" + oldStatus + ",无法修改");
		}
		return rep;
	}

	@Override
	public void deleteByRoomId(String id) {
		GuestRoomStatus status = guestRoomStatusDao.findByGuestRoomId(id);
		if (status != null) {
			delete(status.getId());
		}

	}

	@Override
	public GuestRoomStatusVo detail(String id) {
		GuestRoomStatus status = findById(id);
		return GuestRoomStatusVo.covert(status);
	}

	@Override
	public GuestRoomStatusVo detailGuestRoom(String id) {
		GuestRoomStatus status = guestRoomStatusDao.findByGuestRoomId(id);
		return GuestRoomStatusVo.covert(status);
	}

}
