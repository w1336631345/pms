package com.kry.pms.service.room.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.room.GuestRoomStatusDao;
import com.kry.pms.model.annotation.UpdateAnnotation;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.http.response.room.BuildingVo;
import com.kry.pms.model.http.response.room.FloorVo;
import com.kry.pms.model.http.response.room.GuestRoomStatusVo;
import com.kry.pms.model.http.response.room.RoomStatusTableVo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.model.persistence.room.Building;
import com.kry.pms.model.persistence.room.Floor;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.GuestRoomStatus;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomLockRecordService;
import com.kry.pms.service.log.UpdateLogService;
import com.kry.pms.service.room.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    CheckInRecordService checkInRecordService;
    @Autowired
    RoomLockRecordService roomLockRecordService;
    @Autowired
    UpdateLogService updateLogService;

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
//        return guestRoomStatusDao.saveAndFlush(guestRoomStatus);
        return updateLogService.guestRoomStatusModify(guestRoomStatus);
    }

    @Override
    @UpdateAnnotation(name = "房号", value = "roomNum", type = "RS")
    public GuestRoomStatus modifyLog(GuestRoomStatus guestRoomStatus) {
        GuestRoomStatus grs = findById(guestRoomStatus.getId());
        GuestRoomStatus gs = guestRoomStatusDao.saveAndFlush(guestRoomStatus);
        return gs;
    }

    @Override
    public GuestRoomStatus findById(String id) {
        GuestRoomStatus gs = guestRoomStatusDao.getOne(id);
        return gs;
    }
    @Override
    public GuestRoomStatus logFindById(String id) {
        GuestRoomStatus gs = guestRoomStatusDao.logFindById(id);
        return gs;
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
        status.setHotelCode(guestRoom.getHotelCode());
        status.setRoomNum(guestRoom.getRoomNum());
        status.setCreateDate(LocalDateTime.now());
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
//			List<CheckInRecord> crs = grs.getCurrentCheckInRecords();
//			if (crs != null) {
//				grs.getCurrentCheckInRecords().add(cir);
//			} else {
//				crs = new ArrayList<>();
//				crs.add(cir);
//			}
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
//			grs.setCurrentCheckInRecords(null);
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
//			grs.getCurrentCheckInRecords().add(checkInRecord);
            modify(grs);
        }
    }

    @Override
    public RoomStatusTableVo table(String currentHotleCode) {
        RoomStatusTableVo roomStatusTableVo = new RoomStatusTableVo();
        ArrayList<BuildingVo> data = new ArrayList<>();
        List<Building> list = buildingService.getAllByHotelCode(currentHotleCode);
        ArrayList<FloorVo> fvs = null;
        List<GuestRoomStatusVo> grsvs = null;
        GuestRoomStatusVo grsv = null;
        FloorVo fv = null;
        BuildingVo bv = null;
        for (Building b : list) {
            List<Floor> fs = floorService.findByBuildingId(b.getId(), Constants.DELETED_FALSE);
            if (fs != null && !fs.isEmpty()) {
                fvs = new ArrayList<FloorVo>();
                for (Floor f : fs) {
//					List<GuestRoomStatus> rs = guestRoomStatusDao.queryViewByFloorId(f.getId());
//					if (rs != null && !rs.isEmpty()) {
//						grsvs = new ArrayList<>();
//						for (GuestRoomStatus grs : rs) {
//							grsv = new GuestRoomStatusVo();
//							BeanUtils.copyProperties(grs, grsv);
//							grsv.setRoomNum(grs.getGuestRoom().getRoomNum());
//							grsv.setGuestRoomId(grs.getGuestRoom().getId());
//							grsvs.add(grsv);
//						}
//						fv = new FloorVo();
//						BeanUtils.copyProperties(f, fv);
//						fv.setRooms(grsvs);
//						fvs.add(fv);
//					}
                    grsvs = guestRoomStatusDao.queryViewByFloorId(f.getId());
                    if (grsvs != null && !grsvs.isEmpty()) {
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
                break;
            case Constants.Status.ROOM_STATUS_OCCUPY_DIRTY:
                if (oldStatus.equals(Constants.Status.ROOM_STATUS_OCCUPY_CLEAN)) {
                    return true;
                }
                break;
            case Constants.Status.ROOM_STATUS_VACANT_CLEAN:
                if (oldStatus.equals(Constants.Status.ROOM_STATUS_VACANT_DIRTY)) {
                    return true;
                }
                break;
            case Constants.Status.ROOM_STATUS_VACANT_DIRTY:
                if (oldStatus.equals(Constants.Status.ROOM_STATUS_VACANT_CLEAN)) {
                    return true;
                }
                break;
            case Constants.Status.ROOM_STATUS_OUT_OF_ORDER:
                if (oldStatus.equals(Constants.Status.ROOM_STATUS_VACANT_DIRTY)
                        || oldStatus.equals(Constants.Status.ROOM_STATUS_VACANT_CLEAN)) {
                    return true;
                }
                break;
            case Constants.Status.ROOM_STATUS_OUT_OF_SERVCIE:
                if (oldStatus.equals(Constants.Status.ROOM_STATUS_VACANT_DIRTY)
                        || oldStatus.equals(Constants.Status.ROOM_STATUS_VACANT_CLEAN)) {
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    public DtoResponse<String> changeRoomStatus(String id, String status, int quantity, boolean force) {
        DtoResponse<String> rep = new DtoResponse<String>();
        GuestRoomStatus roomStatus = guestRoomStatusDao.findByGuestRoomId(id);
        String oldStatus = roomStatus.getRoomStatus();
        if (force || statusChangeSure(oldStatus, status)) {
            roomStatus.setRoomStatus(status);
            modify(roomStatus);
            // 该数量改为前端统计
//			roomStatusQuantityService.transformRoomStatusQuantity(roomStatus.getHotelCode(), oldStatus, status, 1);
        } else {
            rep.setStatus(Constants.BusinessCode.CODE_ILLEGAL_OPERATION);
            rep.setMessage(roomStatus.getRoomNum() + ":当前状态为：" + oldStatus + ",无法修改");
        }
        return rep;
    }

    @Override
    public boolean lockGuestRoom(String id, RoomLockRecord record) {
        GuestRoomStatus roomStatus = guestRoomStatusDao.findByGuestRoomId(id);
        roomStatus.setRoomStatus(Constants.Status.ROOM_STATUS_OUT_OF_SERVCIE);
        modify(roomStatus);
        return true;
    }


    @Override
    public boolean unLockGuestRoom(String id, RoomLockRecord record) {
        GuestRoomStatus roomStatus = guestRoomStatusDao.findByGuestRoomId(id);
        roomStatus.setRoomStatus(record.getEndToStatus());
        modify(roomStatus);
        return true;
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
        inflatRecordInfo(status);
        return GuestRoomStatusVo.covert(status);
    }

    @Override
    public GuestRoomStatusVo detailGuestRoom(String id) {
        GuestRoomStatus status = guestRoomStatusDao.findByGuestRoomId(id);
        inflatRecordInfo(status);
        return GuestRoomStatusVo.covert(status);
    }

    @Override
    public void updateSummary(GuestRoom gr, String oldVal, String newVal) {
        GuestRoomStatus status = guestRoomStatusDao.findByGuestRoomId(gr.getId());
        status.setSummary(status.getSummary().replace(oldVal, newVal));
        modify(status);
    }

    private void inflatRecordInfo(GuestRoomStatus status) {
        List<CheckInRecord> currentCheckInRecords = checkInRecordService.findByGuestRoomAndStatusAndDeleted(
                status.getGuestRoom(), Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN, Constants.DELETED_FALSE);
        status.setCurrentCheckInRecords(currentCheckInRecords);
        List<CheckInRecord> willCheckInRecords = checkInRecordService.findTodayCheckInRecord(status.getGuestRoom(),
                Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
        status.setWillCheckInRecords(willCheckInRecords);
        List<RoomLockRecord> rlrs = roomLockRecordService.findByGuestRoomAndStatus(status.getGuestRoom(),
                Constants.Status.NORMAL);
        status.setLockRecords(rlrs);

    }

    @Override
    public void changeStatus(UseInfoAble info) {
        GuestRoomStatus status = guestRoomStatusDao.findByGuestRoomId(info.guestRoom().getId());
        if (status != null) {
            if (info.getRoomStatus() != null) {
                if (Constants.Status.ROOM_STATUS_OCCUPY_CLEAN.equals(info.getRoomStatus())) {
                    status.setFree(info.isFree());
                    status.setGroup(info.isGroup());
                    status.setOta(info.isOTA());
                    status.setHourRoom(info.isHourRoom());
                    status.setOverdued(info.isArrears());
                    status.setVip(info.isVIP());
                    status.setWillArrive(false);
                } else {
                    status.setFree(false);
                    status.setGroup(false);
                    status.setOta(false);
                    status.setHourRoom(false);
                    status.setOverdued(false);
                    status.setWillArrive(info.isTodayArrive());
                }
                status.setRoomStatus(info.getRoomStatus());
            } else {
                if (info.isTodayArrive()) {
                    status.setWillArrive(true);
                }
                if (info.isTodayLeave()) {
                    status.setWillLeave(true);
                }
            }

            modify(status);
        }
    }

    private String nextRoomStatus(String currentStatus) {
        switch (currentStatus) {
            case Constants.Status.ROOM_STATUS_OCCUPY_CLEAN:
                return Constants.Status.ROOM_STATUS_VACANT_CLEAN;
            default:
                return Constants.Status.ROOM_STATUS_VACANT_DIRTY;
        }
    }

    private String convertToRoomStatus(String useStatus) {
        switch (useStatus) {
            case Constants.Status.ROOM_USAGE_CHECK_IN:
                return Constants.Status.ROOM_STATUS_OCCUPY_CLEAN;
            case Constants.Status.ROOM_USAGE_LOCKED:
                return Constants.Status.ROOM_STATUS_OUT_OF_SERVCIE;
            case Constants.Status.ROOM_USAGE_REPARIE:
                return Constants.Status.ROOM_STATUS_OUT_OF_ORDER;
            default:
                return null;
        }
    }

    @Override
    public GuestRoomStatusVo detailGuestRoomNum(String num, String hotleCode) {
        GuestRoomStatus status = guestRoomStatusDao.findTopByRoomNumAndHotelCode(num, hotleCode);
        if (status == null) {
            return null;
        }
        inflatRecordInfo(status);
        return GuestRoomStatusVo.covert(status);
    }

    @Override
    public void clearLockInfo(UseInfoAble info) {
        GuestRoomStatus status = findGuestRoomStatusByGuestRoom(info.guestRoom());
        LocalDateTime now = LocalDateTime.now();
        //徐老板说不会存在，锁定结束仍然是锁定状态的情况，2020-04-17
        if (now.isAfter(info.getStartTime()) && now.isBefore(info.getEndTime())) {
            status.setRoomStatus(info.nextStatus());
            clearStatus(status);
            modify(status);
        }
    }

    @Override
    public void changeOverdued(GuestRoom gr, boolean status) {
        GuestRoomStatus grs = findGuestRoomStatusByGuestRoom(gr);
        grs.setOverdued(status);
        modify(grs);
    }

    @Override
    public void lock(UseInfoAble info) {
        GuestRoomStatus status = findGuestRoomStatusByGuestRoom(info.guestRoom());
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(info.getStartTime()) && now.isBefore(info.getEndTime())) {
            status.setRoomStatus(info.getRoomStatus());
            clearStatus(status);
            modify(status);
        }
    }

    @Override
    public void clearUseInfo(UseInfoAble info) {
        GuestRoomStatus status = findGuestRoomStatusByGuestRoom(info.guestRoom());
        clearStatus(status);
        status.setRoomStatus(Constants.Status.ROOM_STATUS_VACANT_DIRTY);
        modify(status);
    }

    @Override
    public void updateStatus(UseInfoAble info) {
        GuestRoomStatus status = guestRoomStatusDao.findByGuestRoomId(info.guestRoom().getId());
        status.setFree(info.isFree());
        status.setGroup(info.isGroup());
        status.setOta(info.isOTA());
        status.setHourRoom(info.isHourRoom());
        status.setOverdued(info.isArrears());
        status.setVip(info.isVIP());
        status.setWillArrive(false);
        modify(status);
    }

    @Override
    public void changeRoom(GuestRoom guestRoom, GuestRoom newGuestRoom, LocalDateTime changeTime) {
        GuestRoomStatus oldStatus = guestRoomStatusDao.findByGuestRoomId(guestRoom.getId());
        GuestRoomStatus newStatus = guestRoomStatusDao.findByGuestRoomId(newGuestRoom.getId());
        newStatus.setRoomStatus(oldStatus.getRoomStatus());
        copyStatus(oldStatus, newStatus);
        if (oldStatus.getRoomStatus().equals(Constants.Status.ROOM_STATUS_OCCUPY_CLEAN) || oldStatus.getRoomStatus().equals(Constants.Status.ROOM_STATUS_OCCUPY_DIRTY)) {
            oldStatus.setRoomStatus(Constants.Status.ROOM_STATUS_VACANT_DIRTY);
        }
        clearStatus(oldStatus);
        modify(oldStatus);
        modify(newStatus);
    }

    private void copyStatus(GuestRoomStatus src, GuestRoomStatus target) {
        target.setFree(src.getFree());
        target.setGroup(src.getGroup());
        target.setOta(src.getOta());
        target.setHourRoom(src.getHourRoom());
        target.setOverdued(src.getOverdued());
        target.setVip(src.getVip());
        target.setWillArrive(src.getWillArrive());
    }

    private void clearStatus(GuestRoomStatus status) {
        status.setFree(false);
        status.setGroup(false);
        status.setOta(false);
        status.setHourRoom(false);
        status.setOverdued(false);
        status.setWillLeave(false);
        status.setWillArrive(false);
        status.setVip(false);
    }


}
