package com.kry.pms.service.room.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.http.response.busi.CheckInRecordVo;
import com.kry.pms.model.http.response.room.RoomLockRecordVo;
import com.kry.pms.model.http.response.room.RoomRepairRecordVo;
import com.kry.pms.model.http.response.room.RoomUsageListVo;
import com.kry.pms.model.http.response.room.RoomUsageVo;
import com.kry.pms.model.persistence.busi.BookingItem;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomRepairRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomUsage;
import com.kry.pms.service.busi.BookingItemService;
import com.kry.pms.service.busi.BookingRecordService;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomLockRecordService;
import com.kry.pms.service.busi.RoomRepairRecordService;
import com.kry.pms.service.room.GuestRoomStatusService;
import com.kry.pms.service.room.RoomTypeQuantityService;
import com.kry.pms.service.room.RoomUsageService;

@Service
public class RoomUsageServiceImpl implements RoomUsageService {
    @Autowired
    RoomUsageDao roomUsageDao;
    @Autowired
    BookingRecordService bookingRecordService;
    @Autowired
    CheckInRecordService checkInRecordService;

    @Autowired
    RoomTypeQuantityService roomTypeQuantityService;

    @Autowired
    RoomLockRecordService roomLockRecordService;

    @Autowired
    RoomRepairRecordService roomRepairRecordService;
//	@Autowired
//	GuestRoomStatusService guestRoomStatusService;

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
    public List<RoomUsageListVo> queryUsableGuestRooms(String roomTypeId, LocalDateTime startTime,
                                                       LocalDateTime endDateTime) {
        List<RoomUsageListVo> list = roomUsageDao.queryUsableRoomTypeGuestRooms(roomTypeId, startTime, endDateTime);
        return list;
    }

    private void inflateRoomUsageVo(RoomUsageVo roomUsageVo, RoomUsage ru) {
        if (roomUsageVo.getBusinesskey() != null) {
            if (Constants.Status.ROOM_USAGE_LOCKED.equals(roomUsageVo.getUsageStatus())) {
                roomUsageVo.setRoomLockRecordVo(
                        RoomLockRecordVo.convert(roomLockRecordService.findById(roomUsageVo.getBusinesskey())));
            } else if (Constants.Status.ROOM_USAGE_REPARIE.equals(roomUsageVo.getUsageStatus())) {
                roomUsageVo.setRoomRepairRecordVo(
                        RoomRepairRecordVo.convert(roomRepairRecordService.findById(roomUsageVo.getBusinesskey())));
            } else {
                roomUsageVo.setCheckInRecordVo(
                        CheckInRecordVo.convert(checkInRecordService.findByOrderNumAndGuestRoomAndDeleted(
                                roomUsageVo.getBusinesskey(), ru.getGuestRoom(), Constants.DELETED_FALSE)));

            }
        }
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
    public DtoResponse<RoomUsage> use(GuestRoom gr, String status, LocalDateTime startTime, LocalDateTime endTime,
                                      String businesskey, String businessInfo) {
        return use(gr, status, startTime, endTime, businesskey, businessInfo, true);
    }

    public RoomUsage initRoomUsage(GuestRoom gr) {
        RoomUsage ru = new RoomUsage();
        ru.setStartDateTime(LocalDateTime.now());
        return add(ru);
    }

    @Override
    public boolean changeUseStatus(GuestRoom gr, String businessKey, String status) {
        RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(gr.getId(), businessKey);
        if (ru != null) {
            if (!ru.getUsageStatus().equals(status)) {
                if (ru.getHumanCount() > 1) {// 当前在住人数大于1
                    ru.setHumanCount(ru.getHumanCount() - 1);
                    modify(ru);
                    return true;
                } else {
                    roomTypeQuantityService.changeRoomTypeQuantity(gr.getRoomType(),
                            ru.getStartDateTime().toLocalDate(), ru.getEndDateTime().toLocalDate(), ru.getUsageStatus(),
                            status, 1);
                    ru.setUsageStatus(status);
                    ru.setHumanCount(1);
                    modify(ru);
                }
            } else {
                ru.setHumanCount(ru.getHumanCount() + 1);
                modify(ru);
            }
        }
        return true;
    }

    @Override
    public boolean changeUseStatus(GuestRoom gr, String businessKey, String unique, String status) {
        RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(gr.getId(), businessKey);
        if (ru != null) {
            return changeUseStatus(ru, businessKey, unique, status);
        } else {

        }
        return false;
    }

    private boolean changeUseStatus(RoomUsage ru, String businessKey, String uniqueId, String status) {
        if (!ru.getUsageStatus().equals(status)) {
            //+ 表示一个人触发修改
            //- 表示所有人法触发修改
            if (status.startsWith("+")) {

            } else if (status.startsWith("-")) {

            }
        } else {
            if (status.startsWith("+")) {
                ru.setUsageStatus(status);
                ru.getUniqueIds().clear();
                ru.getUniqueIds().add(uniqueId);
                modify(ru);
            } else if (status.startsWith("-")) {

            }
        }
        return true;
    }

    @Override
    public boolean unUse(GuestRoom gr, String businessKey, LocalDateTime endTime, String roomTypeUsage) {
        RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(gr.getId(), businessKey);
        if (ru != null) {
            if (ru.getHumanCount() > 1) {// 当前在住人数大于1
                ru.setHumanCount(ru.getHumanCount() - 1);
                modify(ru);
                return true;
            }
            if (endTime == null) {
                endTime = ru.getStartDateTime();
            }
//            roomTypeQuantityService.changeRoomTypeQuantity(gr.getRoomType(), endTime.toLocalDate(),
//                    ru.getEndDateTime().toLocalDate(), ru.getUsageStatus(), roomTypeUsage, 1);
            if (!ru.getStartDateTime().isBefore(endTime)) { // 开始时间前释放资源 相当于直接取消
                RoomUsage pru = ru.getPreRoomUsage();
                RoomUsage npru = ru.getPostRoomUsage();
                if (pru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_FREE)) {
                    if (npru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_FREE)) {
                        pru.setEndDateTime(npru.getEndDateTime());
                        updateDuration(pru);
                        RoomUsage nnpru = npru.getPostRoomUsage();
                        pru.setPostRoomUsage(nnpru);
                        modify(pru);
                        if (nnpru != null) {
                            nnpru.setPostRoomUsage(pru);
                            modify(nnpru);
                        }
                        ru.setPostRoomUsage(null);
                        modify(ru);
                        delete(npru.getId());
                        delete(ru.getId());
                    } else {
                        pru.setEndDateTime(ru.getEndDateTime());
                        pru.setPostRoomUsage(npru);
                        modify(pru);
                        npru.setPreRoomUsage(pru);
                        modify(npru);
                    }
                } else {
                    if (npru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_FREE)) {
                        npru.setStartDateTime(ru.getStartDateTime());
                        updateDuration(npru);
                        npru.setPreRoomUsage(pru);
                        modify(npru);
                        pru.setPostRoomUsage(npru);
                        modify(pru);
                        delete(ru.getId());
                    } else {
                        ru.setUsageStatus(Constants.Status.ROOM_USAGE_FREE);
                    }
                }
            } else if (ru.getEndDateTime().isAfter(endTime)) {// 开始时间之后 结束时间之前 相当于提前释放资源
                RoomUsage npru = ru.getPostRoomUsage();
                if (npru != null) {
                    if (npru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_FREE)) {
                        npru.setStartDateTime(endTime);
                        updateDuration(npru);
                        ru.setEndDateTime(endTime);
                        updateDuration(ru);
                        modify(ru);
                        modify(npru);
                    } else {
                        RoomUsage nru = new RoomUsage();
                        nru.setUsageStatus(Constants.Status.ROOM_USAGE_FREE);
                        nru.setStartDateTime(endTime);
                        nru.setEndDateTime(ru.getEndDateTime());
                        ru.setEndDateTime(endTime);
                        updateDuration(ru);
                        updateDuration(nru);
                        add(nru);
                        ru.setPostRoomUsage(nru);
                        modify(ru);
                        nru.setPreRoomUsage(ru);
                        nru.setPostRoomUsage(npru);
                        modify(nru);
                        npru.setPreRoomUsage(nru);
                        modify(npru);
                    }
                } else {
                    // 理论上不会出现
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean unUse(GuestRoom gr, String businessKey, LocalDateTime endTime) {
        return unUse(gr, businessKey, endTime, Constants.Status.ROOM_USAGE_FREE);

    }

    @Override
    public RoomUsage use(GuestRoom gr, String status, LocalDateTime startTime, int days, String businesskey,
                         String businessInfo, DtoResponse<String> response) {
        return null;

    }

    private void updateDuration(RoomUsage ru) {
        long d = Duration.between(ru.getStartDateTime(), ru.getEndDateTime()).get(ChronoUnit.SECONDS);
        ru.setDuration(d / 3600);
    }

    @Override
    public List<RoomUsageListVo> queryUsableGuestRoomsByBookItemId(String bookItemId) {
        BookingRecord br = bookingRecordService.findByBookingItemId(bookItemId);
        CheckInRecord cir = checkInRecordService.findById(bookItemId);
        List<RoomUsageVo> data = new ArrayList<RoomUsageVo>();
        return queryUsableGuestRooms(cir.getRoomType().getId(), br.getArriveTime(), br.getLeaveTime());
    }

    @Override
    public List<RoomUsageListVo> queryUsableGuestRoomsByCheckInRecordId(String cid) {
        CheckInRecord cir = checkInRecordService.findById(cid);
        List<RoomUsageVo> data = new ArrayList<RoomUsageVo>();
        if (cir != null) {
            return queryUsableGuestRooms(cir.getRoomType().getId(), cir.getArriveTime(), cir.getLeaveTime());
        }
        return null;
    }

    @Override
    public List<RoomUsageListVo> queryUsableGuestRoomsByCheckInRecordId(String cid, String roomTypeId, String roomNum) {
        CheckInRecord cir = checkInRecordService.findById(cid);
        List<RoomUsageListVo> data = new ArrayList<RoomUsageListVo>();
        if (cir != null) {
            LocalDateTime startTime = LocalDateTime.now();
            if (cir.getArriveTime().isAfter(startTime)) {
                startTime = cir.getArriveTime();
            }
            List<RoomUsage> list = null;
            if (roomNum != null) {
                list = roomUsageDao.queryRoomUsable(roomNum, startTime);
                if (list != null && !list.isEmpty()) {
                    for (RoomUsage ru : list) {
                        data.add(new RoomUsageListVo(ru));
                    }
                    return data;
                }
            } else {
                if (roomTypeId == null) {
                    roomTypeId = cir.getRoomType().getId();
                }
                return queryUsableGuestRooms(roomTypeId, startTime, cir.getLeaveTime());
            }

        }
        return null;
    }

    @Override
    public boolean checkIn(UseInfoAble info) {
        if (info.guestRoom() != null) {// 如果没有房间的入住不占用任何资源，可能为主单入住
            RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(),
                    info.getBusinessKey());
            if (ru == null) { // 该状态为没有预留直接入住
                // 使用房间资源
                use(info, Constants.Status.ROOM_USAGE_CHECK_IN);
                // 使用房类资源
                roomTypeQuantityService.checkInRoomTypeWithoutBook(info.roomType(), info.getStartTime().toLocalDate(),
                        info.getEndTime().toLocalDate(), 1);
            } else {// 有预留
                // 修改房间状态
                if (!ru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_CHECK_IN)) {
                    if (ru.getUniqueIds() == null) {
                        ru.setUniqueIds(new HashSet<String>());
                    } else {
                        ru.getUniqueIds().clear();
                    }
                    ru.getUniqueIds().add(info.uniqueId());
                    roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getStartTime().toLocalDate(),
                            info.getEndTime().toLocalDate(), ru.getUsageStatus(), Constants.Status.ROOM_USAGE_CHECK_IN, 1);
                    ru.setUsageStatus(Constants.Status.ROOM_USAGE_CHECK_IN);
                } else {
                    if (!ru.getUniqueIds().contains(info.uniqueId())) {
                        ru.getUniqueIds().add(info.uniqueId());
                    }
                }
                modify(ru);
            }
        }

        return true;
    }

    @Override
    public boolean cancleCheckIn(UseInfoAble info) {
        if (info.guestRoom() != null) {// 如果没有房间的入住不占用任何资源，可能为主单入住
            RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(),
                    info.getBusinessKey());
            if (ru == null) { // 该状态为没有预留直接入住
                return false;
            } else {// 有预留
                // 修改房间状态
                if (ru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_CHECK_IN)) {
                    if (ru.getUniqueIds() != null && ru.getUniqueIds().contains(info.uniqueId())) {
                        ru.getUniqueIds().remove(info.uniqueId());
                    }
                    if (ru.getUniqueIds().isEmpty()) {
                        roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getStartTime().toLocalDate(),
                                info.getEndTime().toLocalDate(), ru.getUsageStatus(), Constants.Status.ROOM_USAGE_ASSIGN, 1);
                        ru.setUsageStatus(Constants.Status.ROOM_USAGE_ASSIGN);
                    }
                }
                modify(ru);
            }
        }
        return true;
    }

    @Override
    public boolean assignRoom(UseInfoAble info) {
        if (info.guestRoom() != null) {// 如果没有房间的入住不占用任何资源，可能为主单入住
            RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(),
                    info.getBusinessKey());
            if (ru == null) { // 该状态为没有预留直接入住
                // 使用房间资源
                boolean result = use(info, Constants.Status.ROOM_USAGE_ASSIGN);
                if (result) {
                    // 使用房类资源
                    roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getStartTime().toLocalDate(),
                            info.getEndTime().toLocalDate(), Constants.Status.ROOM_USAGE_RESERVATION, Constants.Status.ROOM_USAGE_ASSIGN, 1);
                }
            } else {// 有预留
                // 修改房间状态
                if (!ru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_ASSIGN)) {
                    if (ru.getUniqueIds() == null) {
                        ru.setUniqueIds(new HashSet<String>());
                    } else {
                        ru.getUniqueIds().clear();
                    }
                    ru.getUniqueIds().add(info.uniqueId());
                    roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getStartTime().toLocalDate(),
                            info.getEndTime().toLocalDate(), Constants.Status.ROOM_USAGE_RESERVATION, Constants.Status.ROOM_USAGE_ASSIGN, 1);
                    ru.setUsageStatus(Constants.Status.ROOM_USAGE_ASSIGN);
                } else {
                    if (!ru.getUniqueIds().contains(info.uniqueId())) {
                        ru.getUniqueIds().add(info.uniqueId());
                    }
                }
                modify(ru);
            }
        }
        return true;
    }

    @Override
    public boolean cancleAssignRoom(UseInfoAble info) {
        if (info.guestRoom() != null) {// 如果没有房间的入住不占用任何资源，可能为主单入住
            RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(),
                    info.getBusinessKey());
            if (ru == null) { // 该状态为没有预留直接入住
                return false;
            } else {// 有预留
                // 修改房间状态
                if (ru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_ASSIGN)) {
                    if (ru.getUniqueIds() != null && ru.getUniqueIds().contains(info.uniqueId())) {
                        ru.getUniqueIds().remove(info.uniqueId());
                    }
                    if (ru.getUniqueIds().isEmpty()) {
                        roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getStartTime().toLocalDate(),
                                info.getEndTime().toLocalDate(), ru.getUsageStatus(), Constants.Status.ROOM_USAGE_RESERVATION, 1);
                        unUse(info.guestRoom(),info.getBusinessKey(),LocalDateTime.now());
                    }
                }
//                modify(ru);
            }
        }
        return true;
    }

    @Override
    public boolean checkOut(UseInfoAble info) {
        return false;
    }

    @Override
    public boolean cancleCheckOut(UseInfoAble info) {
        return false;
    }

    private boolean use(UseInfoAble info, String status) {
        RoomUsage ru = roomUsageDao.queryGuestRoomUsable(info.guestRoom().getId(), info.getStartTime(), info.getEndTime());
        LocalDateTime startTime = info.getStartTime();
        LocalDateTime endTime = info.getEndTime();
        Duration d = Duration.between(startTime, endTime);
        long duration = d.get(ChronoUnit.SECONDS) / 3600;
        String businessInfo = info.getSummaryInfo();
        String businesskey = info.getBusinessKey();
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
                    npur.setUniqueIds(null);
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
                    npur.setUniqueIds(null);
                    ru.setEndDateTime(startTime);
                    updateDuration(ru);
                    npur.setStartDateTime(endTime);
                    npur.setUsageStatus(status);
                    npur.setBusinessInfo(businessInfo);
                    npur.setId(null);
                    npur.setBusinesskey(businesskey);
                    ru = modify(ru);
                    npur.setPreRoomUsage(ru);
                    data = add(npur);
                    ru.setPostRoomUsage(npur);
                    modify(ru);
                } else {
                    RoomUsage cru = new RoomUsage();
                    RoomUsage pru = new RoomUsage();
                    BeanUtils.copyProperties(ru, cru);
                    BeanUtils.copyProperties(ru, pru);
                    cru.setUniqueIds(null);
                    pru.setUniqueIds(null);
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
            if (data.getUniqueIds() == null) {
                data.setUniqueIds(new HashSet<String>());
            } else {
                data.getUniqueIds().clear();
            }
            data.getUniqueIds().add(info.uniqueId());
            modify(data);
        }
        return true;
    }


    @Override
    public DtoResponse<RoomUsage> use(GuestRoom gr, String status, LocalDateTime startTime, LocalDateTime endTime,
                                      String businesskey, String businessInfo, boolean roomTypeOperation) {
        DtoResponse<RoomUsage> response = new DtoResponse<RoomUsage>();
        Duration d = Duration.between(startTime, endTime);
        long duration = d.get(ChronoUnit.SECONDS) / 3600;
//		if (status.equals(Constants.Status.ROOM_USAGE_ASSIGN)) {
        RoomUsage eru = roomUsageDao.findByGuestRoomIdAndBusinesskeyAndUsageStatus(gr.getId(), businesskey, status);
        if (eru != null) {
            // 同房间多人多次分房
            eru.setHumanCount(eru.getHumanCount() + 1);
            return response.addData(eru);
        }
//		}
        RoomUsage ru = roomUsageDao.queryGuestRoomUsable(gr.getId(), startTime, endTime);
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
                    npur.setUniqueIds(null);
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
                    npur.setUniqueIds(null);
                    ru.setEndDateTime(startTime);
                    updateDuration(ru);
                    npur.setStartDateTime(endTime);
                    npur.setUsageStatus(status);
                    npur.setBusinessInfo(businessInfo);
                    npur.setId(null);
                    npur.setBusinesskey(businesskey);
                    ru = modify(ru);
                    npur.setPreRoomUsage(ru);
                    data = add(npur);
                    ru.setPostRoomUsage(npur);
                    modify(ru);
                } else {
                    RoomUsage cru = new RoomUsage();
                    RoomUsage pru = new RoomUsage();
                    BeanUtils.copyProperties(ru, cru);
                    BeanUtils.copyProperties(ru, pru);
                    cru.setUniqueIds(null);
                    pru.setUniqueIds(null);
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
            data.setHumanCount(1);
            modify(data);
            roomTypeQuantityService.useRoomType(gr.getRoomType(), startTime.toLocalDate(), endTime.toLocalDate(),
                    status);

        } else {
            response.setStatus(Constants.BusinessCode.CODE_RESOURCE_NOT_ENOUGH);
            response.setMessage("房间号：" + gr.getRoomNum() + "在该时段无法使用，请确认");
        }
        return response.addData(data);
    }

    @Override
    public boolean freeCheck(GuestRoom gr, LocalDateTime startTime, LocalDateTime endDateTime) {
        if (endDateTime != null) {
        }
        return false;
    }

}
