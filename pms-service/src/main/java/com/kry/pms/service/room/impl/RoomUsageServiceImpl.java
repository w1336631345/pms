package com.kry.pms.service.room.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.room.RoomUsageDao;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.http.response.busi.CheckInRecordVo;
import com.kry.pms.model.http.response.room.RoomLockRecordVo;
import com.kry.pms.model.http.response.room.RoomUsageListVo;
import com.kry.pms.model.http.response.room.RoomUsageVo;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomUsage;
import com.kry.pms.service.busi.BookingRecordService;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomLockRecordService;
import com.kry.pms.service.room.GuestRoomStatusService;
import com.kry.pms.service.room.RoomTypeQuantityService;
import com.kry.pms.service.room.RoomUsageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    GuestRoomStatusService guestRoomStatusService;

    @Override
    public RoomUsage add(RoomUsage roomUsage) {
        return roomUsageDao.saveAndFlush(roomUsage);
    }

    @Override
    public void delete(String id) {
        // ????????????
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
        return null;// ???????????????
        // return roomUsageDao.findByHotelCode(code);
    }

    @Override
    public List<RoomUsageListVo> queryByRoomType(String roomTypeId, LocalDateTime startTime,
                                                 LocalDateTime endDateTime) {
        List<RoomUsageListVo> list = roomUsageDao.queryByRoomType(roomTypeId, startTime, endDateTime);
        return list;
    }
    @Override
    public List<RoomUsageListVo> queryByRoomType2(String hotelCode, String[] ids, LocalDateTime startTime,
                                                 LocalDateTime endDateTime) {
        List<String> lids = null;
        if (ids != null) {
            lids = Arrays.asList(ids);
        }
        List<RoomUsageListVo> list = roomUsageDao.queryByRoomType2(hotelCode,lids, startTime, endDateTime);
        return list;
    }

    //??????????????????????????????????????????????????????????????????????????????
    @Override
    public List<RoomUsageListVo> queryUsableGuestRooms(String roomTypeId, LocalDateTime startTime,
                                                       LocalDateTime endDateTime) {
        List<RoomUsageListVo> list = roomUsageDao.queryUsableRoomTypeGuestRooms(roomTypeId, startTime, endDateTime);
        return list;
    }

    //??????????????????????????????????????????????????????????????????????????????
    @Override
    public List<RoomUsageListVo> queryUsableGuestRoomsTo(String roomTypeId, LocalDateTime startTime,
                                                         LocalDateTime endDateTime) {
        List<RoomUsageListVo> list = roomUsageDao.queryUsableRoomTypeGuestRooms(roomTypeId, LocalDateTime.now(), endDateTime);
        return list;
    }

    private void inflateRoomUsageVo(RoomUsageVo roomUsageVo, RoomUsage ru) {
        if (roomUsageVo.getBusinesskey() != null) {
            if (Constants.Status.ROOM_USAGE_LOCKED.equals(roomUsageVo.getUsageStatus())) {
                roomUsageVo.setRoomLockRecordVo(
                        RoomLockRecordVo.convert(roomLockRecordService.findById(roomUsageVo.getBusinesskey())));
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
                if (ru.getHumanCount() > 1) {// ????????????????????????1
                    ru.setHumanCount(ru.getHumanCount() - 1);
                    modify(ru);
                    return true;
                } else {
                    roomTypeQuantityService.changeRoomTypeQuantity(gr.getRoomType(),
                            ru.getStartDateTime(), ru.getEndDateTime(), ru.getUsageStatus(),
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
            //+ ???????????????????????????
            //- ??????????????????????????????
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
//            if (ru.getHumanCount() > 1) {// ????????????????????????1
//                ru.setHumanCount(ru.getHumanCount() - 1);
//                modify(ru);
//                return true;
//            }
            if (endTime == null) {
                endTime = ru.getStartDateTime();
            }
//            roomTypeQuantityService.changeRoomTypeQuantity(gr.getRoomType(), endTime.toLocalDate(),
//                    ru.getEndDateTime().toLocalDate(), ru.getUsageStatus(), roomTypeUsage, 1);
            if (!ru.getStartDateTime().isBefore(endTime)) { // ??????????????????????????? ?????????????????????
                RoomUsage pru = ru.getPreRoomUsage();//?????????
                RoomUsage npru = ru.getPostRoomUsage();//?????????
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
                        ru.setPostRoomUsage(null);
                        ru.setPreRoomUsage(null);
                        modify(ru);
                        delete(ru.getId());
                    } else {
                        ru.setUsageStatus(Constants.Status.ROOM_USAGE_FREE);
                    }
                }
            } else if (ru.getEndDateTime().isAfter(endTime)) {// ?????????????????? ?????????????????? ???????????????????????????
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
                    // ?????????????????????
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
                return queryUsableGuestRooms(roomTypeId, startTime, LocalDateTime.of(cir.getLeaveTime().toLocalDate(), LocalTime.NOON));
            }

        }
        return null;
    }

    @Override
    public List<Map<String, Object>> queryUsableGuestRoomsByCheckInRecordIdNew(String cid, String floorId, String buildingId) {
        CheckInRecord cir = checkInRecordService.findById(cid);
        if (cir != null) {
            LocalDateTime startTime = LocalDateTime.now();
            if (cir.getArriveTime().isAfter(startTime)) {
                startTime = cir.getArriveTime();
            }
//            LocalDateTime startTime = cir.getArriveTime();
            String roomTypeId = cir.getRoomType().getId();
            List<Map<String, Object>> list = roomUsageDao.queryUsableRoomTypeGuestRoomsNew(roomTypeId, startTime, LocalDateTime.of(cir.getLeaveTime().toLocalDate(), LocalTime.NOON), floorId, buildingId);
            return list;
        }
        return null;
    }

    @Override
    public boolean checkIn(UseInfoAble info) {
        if (info.guestRoom() != null) {// ????????????????????????????????????????????????????????????????????????
            RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(),
                    info.getBusinessKey());
            if (ru == null) { // ????????????????????????????????????
                // ??????????????????
                use(info, Constants.Status.ROOM_USAGE_CHECK_IN);
                guestRoomStatusService.changeStatus(info);
                // ??????????????????
                roomTypeQuantityService.checkInRoomTypeWithoutBook(info.roomType(), info.getStartTime().toLocalDate(),
                        info.getEndTime().toLocalDate(), 1);
            } else {// ?????????
                // ??????????????????
                if (!ru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_CHECK_IN)) {
                    if (ru.getUniqueIds() == null) {
                        ru.setUniqueIds(new HashSet<String>());
                    } else {
                        ru.getUniqueIds().clear();
                    }
                    ru.getUniqueIds().add(info.uniqueId());
                    guestRoomStatusService.changeStatus(info);
                    roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getStartTime(),
                            info.getEndTime(), ru.getUsageStatus(), Constants.Status.ROOM_USAGE_CHECK_IN, 1);
                    ru.setUsageStatus(Constants.Status.ROOM_USAGE_CHECK_IN);
                } else {
                    if (!ru.getUniqueIds().contains(info.uniqueId())) {
                        ru.getUniqueIds().add(info.uniqueId());
                    }
                }
                modify(ru);
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean cancleCheckIn(UseInfoAble info) {
        if (info.guestRoom() != null) {// ????????????????????????????????????????????????????????????????????????
            RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(),info.getBusinessKey());
            if (ru == null) { // ????????????????????????????????????
                return false;
            } else {// ?????????
                // ??????????????????
                if (ru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_CHECK_IN)) {
                    if (ru.getUniqueIds() != null && ru.getUniqueIds().contains(info.uniqueId())) {
                        ru.getUniqueIds().remove(info.uniqueId());
                    }
                    if (ru.getUniqueIds().isEmpty()) {
                        roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getStartTime(),
                                info.getEndTime(), ru.getUsageStatus(), Constants.Status.ROOM_USAGE_ASSIGN, 1);
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
        if (info.guestRoom() != null) {// ????????????????????????????????????????????????????????????????????????
            RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(),
                    info.getBusinessKey());
            if (ru == null) {
                // ??????????????????
                boolean result = use(info, Constants.Status.ROOM_USAGE_ASSIGN);
                if (result) {
                    // ??????????????????
                    roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getStartTime(),
                            info.getEndTime(), Constants.Status.ROOM_USAGE_RESERVATION, Constants.Status.ROOM_USAGE_ASSIGN, 1);  //?????????ru??????true?????????????????????????????????,??????????????????R???????????????????????????????????????
                } else {
                    return false;
                }
            } else {// ?????????
                // ??????????????????
                if (!ru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_ASSIGN)) {
                    if (ru.getUniqueIds() == null) {
                        ru.setUniqueIds(new HashSet<String>());
                    } else {
                        ru.getUniqueIds().clear();
                    }
                    ru.getUniqueIds().add(info.uniqueId());
                    roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getStartTime(),
                            info.getEndTime(), Constants.Status.ROOM_USAGE_RESERVATION, Constants.Status.ROOM_USAGE_ASSIGN, 1);
                    ru.setUsageStatus(Constants.Status.ROOM_USAGE_ASSIGN);
                } else {
                    if (!ru.getUniqueIds().contains(info.uniqueId())) {
                        ru.getUniqueIds().add(info.uniqueId());
                        modify(ru);
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean cancleAssignRoom(UseInfoAble info) {
        if (info.guestRoom() != null) {// ????????????????????????????????????????????????????????????????????????
            RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(),
                    info.getBusinessKey());
            if (ru == null) { // ????????????????????????????????????
                return false;
            } else {// ?????????
                ru.setRemark("????????????");
                // ??????????????????
                if (ru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_ASSIGN)) {
                    if (ru.getUniqueIds() != null && ru.getUniqueIds().contains(info.uniqueId())) {
                        ru.getUniqueIds().remove(info.uniqueId());
                    }
                    if (ru.getUniqueIds().isEmpty()) {
                        roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), ru.getStartDateTime(),
                                ru.getEndDateTime(), ru.getUsageStatus(), Constants.Status.ROOM_USAGE_RESERVATION, 1);
                        guestRoomStatusService.cancelAssignRoom(info);
                        unUse(ru,LocalDateTime.now());
                    } else {
                        modify(ru);
                    }
                }
            }
        }
        return true;
    }

    private boolean unUse(RoomUsage ru, LocalDateTime unUseTime) {
        if (!ru.getStartDateTime().isBefore(unUseTime)) { // ??????????????????????????? ?????????????????????
            RoomUsage pru = ru.getPreRoomUsage();//?????????
            RoomUsage npru = ru.getPostRoomUsage();//?????????
            if (pru != null && pru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_FREE)) {
                if (npru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_FREE)) {
                    pru.setEndDateTime(npru.getEndDateTime());
                    updateDuration(pru);
                    RoomUsage nnpru = npru.getPostRoomUsage();
                    pru.setPostRoomUsage(nnpru);
                    modify(pru);
                    if (nnpru != null) {
                        nnpru.setPreRoomUsage(pru);
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
                return true;
            } else {
                if (npru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_FREE)) {
                    npru.setStartDateTime(ru.getStartDateTime());
                    updateDuration(npru);
                    npru.setPreRoomUsage(pru);
                    modify(npru);
                    if (pru != null) {
                        pru.setPostRoomUsage(npru);
                        modify(pru);
                    }
                    ru.setPostRoomUsage(null);
                    ru.setPreRoomUsage(null);
                    modify(ru);
                    delete(ru.getId());
                } else {
                    ru.setUsageStatus(Constants.Status.ROOM_USAGE_FREE);
                }
            }
        } else if (ru.getEndDateTime().isAfter(unUseTime)) {// ?????????????????? ?????????????????? ???????????????????????????
            RoomUsage npru = ru.getPostRoomUsage();
            if (npru != null) {
                if (npru.getUsageStatus().equals(Constants.Status.ROOM_USAGE_FREE)) {
                    npru.setStartDateTime(unUseTime);
                    updateDuration(npru);
                    ru.setEndDateTime(unUseTime);
                    updateDuration(ru);
                    modify(ru);
                    modify(npru);
                } else {
                    RoomUsage nru = new RoomUsage();
                    nru.setUsageStatus(Constants.Status.ROOM_USAGE_FREE);
                    nru.setStartDateTime(unUseTime);
                    nru.setEndDateTime(ru.getEndDateTime());
                    ru.setEndDateTime(unUseTime);
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
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
        return false;
    }

    private boolean unUse(RoomUsage ru) {
        return unUse(ru, ru.getStartDateTime());

    }

    @Override
    public boolean checkOut(UseInfoAble info) {
        if (info.guestRoom() != null) {
            RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(),
                    info.getBusinessKey());
            if (ru != null) {
                ru.setRemark("??????");
                if (ru.getUniqueIds() != null && ru.getUniqueIds().contains(info.uniqueId())) {
                    ru.getUniqueIds().remove(info.uniqueId());
                }
                if (ru.getUniqueIds() == null || ru.getUniqueIds().isEmpty()) {
                    if (info.getEndTime().equals(ru.getEndDateTime())) {
                        ru.setUsageStatus(Constants.Status.ROOM_USAGE_CHECK_OUT);
                    } else {
                        if (info.getEndTime().isBefore(ru.getEndDateTime())) {
                            roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getEndTime(),
                                    ru.getEndDateTime(), ru.getUsageStatus(), Constants.Status.ROOM_USAGE_PREDICTABLE, 1);
                            unUse(ru,LocalDateTime.now());
                        }
                    }
                    guestRoomStatusService.clearUseInfo(info);
                }
            } else {
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean cancleCheckOut(UseInfoAble info) {
        return false;
    }

    @Override
    public boolean lock(UseInfoAble info) {
        if (use(info, info.useType())) {
            roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getStartTime(),
                    info.getEndTime(), Constants.Status.ROOM_USAGE_AVAILABLE, info.useType(), 1);
            guestRoomStatusService.lock(info);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean unLock(UseInfoAble info, LocalDateTime cancleDateTime) {
        RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(),
                info.getBusinessKey());
        if (ru != null) {
            ru.setRemark("??????????????????");
            if (unUse(ru, cancleDateTime)) {
                if (!cancleDateTime.isAfter(info.getStartTime())) {
                    roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), info.getStartTime(),
                            info.getEndTime(), ru.getUsageStatus(), Constants.Status.ROOM_USAGE_AVAILABLE, 1);
                } else if (!cancleDateTime.isAfter(info.getEndTime())) {
                    roomTypeQuantityService.changeRoomTypeQuantity(info.roomType(), cancleDateTime,
                            info.getEndTime(), ru.getUsageStatus(), Constants.Status.ROOM_USAGE_AVAILABLE, 1);
                }
                guestRoomStatusService.clearLockInfo(info);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean extendTime(UseInfoAble info, LocalDate extendDate) {
        RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(), info.getBusinessKey());
        String status = ru.getUsageStatus();
        ru.setRemark("????????????");
        unUse(ru);
        LocalDateTime endTime = LocalDateTime.of(extendDate, LocalTime.NOON);
        info.setEndTime(endTime);
        return use(info, status);
    }

    @Override
    public boolean extendTime(UseInfoAble info, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(), info.getBusinessKey());
        LocalDateTime endTime = newEndTime;
        LocalDateTime startTime = newStartTime;
        String status = ru.getUsageStatus();
        ru.setRemark("????????????");
        unUse(ru);
        info.setStartTime(startTime);
        info.setEndTime(endTime);
        return use(info, status);
    }

    @Override
    public boolean extendTime2(UseInfoAble info, LocalDateTime newEndTime) {
        RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(), info.getBusinessKey());
        if(ru == null){
            return false;
        }
        //????????????????????????????????????
        if(newEndTime.isBefore(ru.getStartDateTime())){
            return false;
        }
        //???????????????????????????
        RoomUsage post = ru.getPostRoomUsage();
        //???????????????????????????????????????post???????????????????????? >= newEndTime
        if(newEndTime.isBefore(post.getEndDateTime()) || newEndTime.isEqual(post.getEndDateTime())){
            ru.setEndDateTime(newEndTime);
            roomUsageDao.saveAndFlush(ru);
            post.setStartDateTime(newEndTime);//??????????????????????????????????????????????????????????????????????????????
            roomUsageDao.saveAndFlush(post);
            return true;
        }else {
            return false;
        }

    }

    @Override
    public boolean changeRoom(UseInfoAble info, GuestRoom newGuestRoom, LocalDateTime changeTime) {
        RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(), info.getBusinessKey());
        if(changeTime.isBefore(info.getStartTime())){
            changeTime = info.getStartTime();
        }
        if (ru != null) {
            ru.setRemark("??????");
            ru.setBusinesskey(ru.getBusinesskey()+"_C");//?????????????????????????????????????????????????????????????????????????????????roomUsage?????????
            boolean u = unUse(ru, changeTime);
            boolean use = use(newGuestRoom, ru.getUsageStatus(), changeTime, info.getEndTime(), info.getBusinessKey(), info.getSummaryInfo(), info.uniqueId());
            if (u) {
                return use;
            }
        }
        return false;
    }

    @Override
    public boolean addTogether(UseInfoAble info) {
        RoomUsage ru = roomUsageDao.findByGuestRoomIdAndBusinesskey(info.guestRoom().getId(), info.getBusinessKey());
        if (ru != null) {
            ru.getUniqueIds().add(info.uniqueId());
            modify(ru);
            return true;
        } else {
            return false;
        }
    }

    private boolean use(GuestRoom gr, String status, LocalDateTime startTime, LocalDateTime endTime,
                        String businesskey, String businessInfo, String uniqueId) {
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
                    npur.setStartDateTime(endTime);
                    npur.setPreRoomUsage(ru);
                    npur.setId(null);
                    updateDuration(npur);
                    add(npur);
                    ru.setPostRoomUsage(npur);
                    updateDuration(ru);
                    data = modify(ru);
                }
            } else {
                if (ru.getEndDateTime().isEqual(endTime)) {
                    RoomUsage npur = new RoomUsage();
                    BeanUtils.copyProperties(ru, npur);
                    npur.setUniqueIds(null);
                    ru.setEndDateTime(startTime);
                    ru.setUsageStatus(status);
                    ru.setBusinessInfo(businessInfo);
                    updateDuration(ru);
                    npur.setStartDateTime(endTime);
                    npur.setId(null);
                    npur.setBusinesskey(businesskey);
                    ru = modify(ru);
                    npur.setPreRoomUsage(ru);
                    updateDuration(npur);
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
                    updateDuration(cru);
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
            data.getUniqueIds().add(uniqueId);
            modify(data);
        } else {
            return false;
        }
        return true;
    }


    private boolean use(UseInfoAble info, String status) {
        RoomUsage ru = roomUsageDao.queryGuestRoomUsable(info.guestRoom().getId(), info.getStartTime(), info.getEndTime());
        LocalDateTime startTime = info.getStartTime();
        LocalDateTime endTime = info.getEndTime();
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
                    ru.setStartDateTime(startTime);
                    ru.setBusinessInfo(businessInfo);
                    ru.setBusinesskey(businesskey);
                    ru.setUsageStatus(status);
                    ru = modify(ru);
                    npur.setStartDateTime(endTime);
                    npur.setPreRoomUsage(ru);
                    npur.setId(null);
                    updateDuration(npur);
                    add(npur);
                    ru.setPostRoomUsage(npur);
                    updateDuration(ru);
                    data = modify(ru);
                }
            } else {
                if (ru.getEndDateTime().isEqual(endTime)) {
                    RoomUsage npur = new RoomUsage();
                    BeanUtils.copyProperties(ru, npur);
                    npur.setUniqueIds(null);
                    ru.setEndDateTime(startTime);
                    ru.setBusinessInfo(businessInfo);
                    updateDuration(ru);
                    npur.setStartDateTime(endTime);
                    npur.setId(null);
                    npur.setUsageStatus(status);
                    npur.setBusinesskey(businesskey);
                    ru = modify(ru);
                    npur.setPreRoomUsage(ru);
                    updateDuration(npur);
                    data = add(npur);
                    ru.setPostRoomUsage(npur);
                    modify(ru);
                } else {
                    RoomUsage cru = copyRoomUsage(ru);
                    RoomUsage pru = copyRoomUsage(ru);
                    cru.setStartDateTime(startTime);
                    cru.setEndDateTime(endTime);
                    cru.setBusinessInfo(businessInfo);
                    cru.setBusinesskey(businesskey);
                    updateDuration(cru);
                    cru.setUsageStatus(status);
                    cru = add(cru);
                    cru.setPreRoomUsage(ru);
                    ru.setEndDateTime(startTime);
                    updateDuration(ru);
                    ru.setPostRoomUsage(cru);
                    modify(ru);
                    pru.setStartDateTime(endTime);
                    pru.setPreRoomUsage(cru);
                    updateDuration(pru);
                    pru = add(pru);
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
        } else {
            return false;
        }
        return true;
    }

    private RoomUsage copyRoomUsage(RoomUsage ru) {
        RoomUsage data = new RoomUsage();
        data.setHotelCode(ru.getHotelCode());
        data.setGuestRoom(ru.getGuestRoom());
        data.setEndDateTime(ru.getEndDateTime());
        data.setStartDateTime(ru.getStartDateTime());
        data.setBusinesskey(ru.getBusinesskey());
        data.setUsageStatus(ru.getUsageStatus());
        return data;
    }

    @Override
    public boolean freeCheck(GuestRoom gr, LocalDateTime startTime, LocalDateTime endDateTime) {
        RoomUsage data = null;
        if (endDateTime != null) {
            data = roomUsageDao.queryGuestRoomUsable(gr.getId(), startTime, endDateTime);
        } else {
            data = roomUsageDao.queryGuestRoomUsable(gr.getId(), startTime);
        }
        return data != null;
    }

    @Override
    public List<Map<String, Object>> miniRoomStatus(String hotelCode, String startTime, String endDateTime,
                                                    String will_arrive, String will_leave, String hour_room,
                                                    String group_, String overdued, String ota, String vip,
                                                    String floorId, String buidId, String roomTypeId, String roomNum) {

        DateTimeFormatter df = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );
        LocalDateTime st = LocalDateTime.now();
        LocalDateTime et = LocalDateTime.now().plusDays(30);
        if(startTime != null){
            st = LocalDateTime.parse(startTime, df);
        }
        if(endDateTime != null){
            et = LocalDateTime.parse(endDateTime, df);
        }
        List<Map<String, Object>> list = roomUsageDao.miniRoomStatus(hotelCode, st, et, will_arrive, will_leave,
                hour_room, group_, overdued, ota, vip, floorId, buidId, roomTypeId, roomNum);
        return list;
    }

    @Override
    public int updateUniqueId(String oldId, String newId){
        int i = roomUsageDao.updateUniqueId(oldId, newId);
        return i;
    }

}
