package com.kry.pms.service.room.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.Constants.BusinessCode;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.marketing.RoomPriceSchemeDao;
import com.kry.pms.dao.room.RoomTypeQuantityDao;
import com.kry.pms.model.func.UseInfoAble;
import com.kry.pms.model.http.response.busi.RoomTypeQuantityPredictableVo;
import com.kry.pms.model.http.response.room.RoomTypeQuantityVo;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.model.persistence.room.RoomTypeQuantity;
import com.kry.pms.model.persistence.sys.BusinessSeq;
import com.kry.pms.service.room.RoomTypeQuantityService;
import com.kry.pms.service.room.RoomTypeService;
import com.kry.pms.service.sys.BusinessSeqService;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

@Service
public class RoomTypeQuantityServiceImpl implements RoomTypeQuantityService {
    @Autowired
    RoomTypeQuantityDao roomTypeQuantityDao;
    @Autowired
    RoomTypeService roomTypeService;
    @Autowired
    RoomPriceSchemeDao roomPriceSchemeDao;
    @Autowired
    BusinessSeqService businessSeqService;
    @Autowired
    EntityManager entityManager;

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
//		rtq.setRoomTypeCode(roomType.getCode());
//		rtq.setRoomTypeName(roomType.getName());
        rtq.setQuantityDate(quantityDate);
        rtq.setUsedTotal(0);
        rtq.setWillArriveTotal(0);
        rtq.setHseTotal(0);
        rtq.setRepairTotal(0);
        rtq.setReserveTotal(0);
        rtq.setLockedTotal(0);
        rtq.setHotelCode(roomType.getHotelCode());
        rtq.setWillLeaveTotal(0);
        rtq.setBookingTotal(0);
        rtq.setAvailableTotal(0);
        rtq.setPredictableTotal(0);
        rtq.setRoomCount(roomType.getRoomCount() != null ? roomType.getRoomCount() : 0);
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

    public void useRoomType(RoomType roomType, LocalDateTime startTime, LocalDateTime endTime, String useType) {
        LocalDate startDate = startTime.toLocalDate();
        LocalDate endDate = endTime.toLocalDate();
        useRoomType(roomType, startDate, endDate, useType);
    }

    @Transactional
    @Override
    public boolean useRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, String useType) {
        return useRoomType(roomType, startDate, endDate, useType, 1);

    }

    public boolean useRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, String useType, int quantity) {
        switch (useType) {
            case Constants.Status.ROOM_USAGE_BOOK:
                RoomTypeQuantityPredictableVo rqpv = queryPredic(roomType.getHotelCode(),roomType.getId(),startDate,endDate);
                int availableTotal = rqpv.getAvailableTotal();
                if(roomType.getOverReservation()!=null){
                    availableTotal+=roomType.getOverReservation();
                }
                if(availableTotal>quantity){
                    bookRoomType(roomType, startDate, endDate, quantity);
                }else{
                    return false;
                }
                break;
            case Constants.Status.ROOM_USAGE_ASSIGN:
                assignRoomType(roomType, startDate, endDate, quantity);
                break;
            case Constants.Status.ROOM_USAGE_CHECK_IN:
                checkInRoomType(roomType, startDate, endDate, quantity);
                break;
            case Constants.Status.ROOM_USAGE_LOCKED:
                lockRoomType(roomType, startDate, endDate, quantity);
                break;
            case Constants.Status.ROOM_USAGE_REPARIE:
                repairRoomType(roomType, startDate, endDate, quantity);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void unUseRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, String useType) {
        unUseRoomType(roomType, startDate, endDate, useType, 1);
    }

    public void unUseRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, String useType, int quantity) {
        switch (useType) {
            case Constants.Status.ROOM_USAGE_BOOK:
                bookRoomType(roomType, startDate, endDate, quantity);
                break;
            case Constants.Status.ROOM_USAGE_ASSIGN:
                assignRoomType(roomType, startDate, endDate, quantity);
                break;
            case Constants.Status.ROOM_USAGE_CHECK_IN:
                unCheckInRoomType(roomType, startDate, endDate, quantity);
                break;
            case Constants.Status.ROOM_USAGE_LOCKED:
                lockRoomType(roomType, startDate, endDate, quantity);
                break;
            case Constants.Status.ROOM_USAGE_REPARIE:
                repairRoomType(roomType, startDate, endDate, quantity);
                break;
            default:
                break;
        }
    }

    @Override
    public void changeRoomTypeQuantity(RoomType roomType, LocalDate startDate, LocalDate endDate, String oldUsageStatus,
                                       String newUsageStatus, int quantity) {
        changeRoomTypeQuantity(roomType, startDate, endDate, oldUsageStatus, newUsageStatus, quantity, true);
    }

    private void changeRoomTypeQuantity(RoomType roomType, LocalDate startDate, LocalDate endDate, String oldUsageStatus,
                                        String newUsageStatus, int quantity, boolean arriveCheck) {
        RoomTypeQuantity rtq = null;
        LocalDate currentDate = startDate;
        int i = 0;
        while (currentDate.isBefore(endDate)) {
            rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
            plusQuantity(rtq, oldUsageStatus, newUsageStatus, quantity);
            if (arriveCheck && i == 0) {
                if ((Constants.Status.ROOM_USAGE_ASSIGN.equals(newUsageStatus) || Constants.Status.ROOM_USAGE_RESERVATION.equals(newUsageStatus)) && (Constants.Status.ROOM_USAGE_CHECK_IN.equals(oldUsageStatus) || Constants.Status.ROOM_USAGE_FREE.equals(oldUsageStatus))) {
                    rtq.setWillArriveTotal(rtq.getWillArriveTotal() + quantity);
                } else if (Constants.Status.ROOM_USAGE_PREDICTABLE.equals(newUsageStatus) && (Constants.Status.ROOM_USAGE_ASSIGN.equals(oldUsageStatus) || Constants.Status.ROOM_USAGE_RESERVATION.equals(oldUsageStatus))) {
                    rtq.setWillArriveTotal(rtq.getWillArriveTotal() - quantity);
                } else if (Constants.Status.ROOM_USAGE_CHECK_IN.equals(newUsageStatus) || (Constants.Status.ROOM_USAGE_FREE.equals(newUsageStatus) && Constants.Status.ROOM_USAGE_ASSIGN.equals(oldUsageStatus))) {
                    rtq.setWillArriveTotal(rtq.getWillArriveTotal() - quantity);
                }
            }
            currentDate = currentDate.plusDays(1);
            modify(rtq);
            i++;
        }
    }


    private void plusQuantity(RoomTypeQuantity rtq, String oldUsageStatus, String newUsageStatus, int quantity) {
        switch (oldUsageStatus) {
            case Constants.Status.ROOM_USAGE_CHECK_IN:
                rtq.setUsedTotal(rtq.getUsedTotal() - quantity);
                break;
            case Constants.Status.ROOM_USAGE_ASSIGN:
                rtq.setBookingTotal(rtq.getBookingTotal() - quantity);
                break;
            case Constants.Status.ROOM_USAGE_BOOK:
                rtq.setReserveTotal(rtq.getReserveTotal() - quantity);
                break;
            case Constants.Status.ROOM_USAGE_FREE:
                rtq.setPredictableTotal(rtq.getPredictableTotal() - quantity);
                break;
            case Constants.Status.ROOM_USAGE_PREDICTABLE:
                rtq.setPredictableTotal(rtq.getPredictableTotal() - quantity);
            case Constants.Status.ROOM_USAGE_CHECK_OUT:
//			rtq.setPredictableTotal(rtq.getPredictableTotal() + quantity);
                break;
            case Constants.Status.ROOM_USAGE_LOCKED:
                rtq.setLockedTotal(rtq.getLockedTotal() - quantity);
                break;
            case Constants.Status.ROOM_USAGE_REPARIE:
                rtq.setRepairTotal(rtq.getRepairTotal() - quantity);
                break;
            case Constants.Status.ROOM_USAGE_RESERVATION:
                rtq.setReserveTotal(rtq.getReserveTotal() - quantity);
                break;
            default:
                break;
        }
        switch (newUsageStatus) {
            case Constants.Status.ROOM_USAGE_CHECK_IN:
                rtq.setUsedTotal(rtq.getUsedTotal() + quantity);
                break;
            case Constants.Status.ROOM_USAGE_ASSIGN:
                rtq.setBookingTotal(rtq.getBookingTotal() + quantity);
                break;
            case Constants.Status.ROOM_USAGE_BOOK:
                rtq.setReserveTotal(rtq.getReserveTotal() + quantity);
                break;
            case Constants.Status.ROOM_USAGE_FREE:
                rtq.setPredictableTotal(rtq.getPredictableTotal() + quantity);
                break;
            case Constants.Status.ROOM_USAGE_PREDICTABLE:
                rtq.setPredictableTotal(rtq.getPredictableTotal() + quantity);
            case Constants.Status.ROOM_USAGE_CHECK_OUT:
//			rtq.setPredictableTotal(rtq.getPredictableTotal() + quantity);
                break;
            case Constants.Status.ROOM_USAGE_LOCKED:
                rtq.setLockedTotal(rtq.getLockedTotal() + quantity);
                break;
            case Constants.Status.ROOM_USAGE_REPARIE:
                rtq.setRepairTotal(rtq.getRepairTotal() + quantity);
                break;
            case Constants.Status.ROOM_USAGE_RESERVATION:
                rtq.setReserveTotal(rtq.getReserveTotal() + quantity);
                break;
            default:
                break;
        }

    }

    private void unCheckInRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
        RoomTypeQuantity rtq = null;
        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate)) {
            rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
            rtq.setBookingTotal(rtq.getBookingTotal() + quantity);
            rtq.setUsedTotal(rtq.getUsedTotal() - quantity);
            currentDate = currentDate.plusDays(1);
            modify(rtq);
        }
    }

    private void bookRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
        RoomTypeQuantity rtq = null;
        LocalDate currentDate = startDate;
        int i = 0;
        while (currentDate.isBefore(endDate)) {
            rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
            if (i == 0) {
                rtq.setWillArriveTotal(rtq.getWillArriveTotal() + quantity);
            }
            rtq.setPredictableTotal(rtq.getPredictableTotal() - quantity);
            rtq.setReserveTotal(rtq.getReserveTotal() + quantity);
            currentDate = currentDate.plusDays(1);
            modify(rtq);
            i++;
        }
        rtq.setWillLeaveTotal(rtq.getWillLeaveTotal() + quantity);
        modify(rtq);
    }

    private void assignRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
        RoomTypeQuantity rtq = null;
        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate)) {
            rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
            rtq.setReserveTotal(rtq.getReserveTotal() - quantity);
            rtq.setBookingTotal(rtq.getBookingTotal() + quantity);
            currentDate = currentDate.plusDays(1);
            modify(rtq);
        }
    }

    private void lockRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
        RoomTypeQuantity rtq = null;
        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate)) {
            rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
            rtq.setAvailableTotal(rtq.getAvailableTotal() - quantity);
            rtq.setLockedTotal(rtq.getLockedTotal() + quantity);
            currentDate = currentDate.plusDays(1);
            modify(rtq);
        }
    }

    private void repairRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
        RoomTypeQuantity rtq = null;
        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate)) {
            rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
            rtq.setAvailableTotal(rtq.getAvailableTotal() - quantity);
            rtq.setRepairTotal(rtq.getRepairTotal() + quantity);
            currentDate = currentDate.plusDays(1);
            modify(rtq);
        }
    }

    private void checkInRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
        RoomTypeQuantity rtq = null;
        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate)) {
            rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
            rtq.setBookingTotal(rtq.getBookingTotal() - quantity);
            rtq.setUsedTotal(rtq.getUsedTotal() + quantity);
            currentDate = currentDate.plusDays(1);
            modify(rtq);
        }
    }

    @Override
    public void checkInRoomTypeWithoutBook(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {
        RoomTypeQuantity rtq = null;
        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate)) {
            rtq = findByRoomTypeAndQuantityDateForUpdate(roomType, currentDate);
            rtq.setPredictableTotal(rtq.getPredictableTotal() - quantity);
            rtq.setUsedTotal(rtq.getUsedTotal() + quantity);
            currentDate = currentDate.plusDays(1);
            modify(rtq);
        }
    }

    public void unUseRoomType(RoomType roomType, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDate startDate = startTime.toLocalDate();
        LocalDate endDate = endTime.toLocalDate();
    }

    public void unUseRoomType(RoomType roomType, LocalDate startDate, LocalDate endDate, int quantity) {

    }

    @Override
    public void checkIn(GuestRoom gr, LocalDate startDate, Integer days) {
        RoomTypeQuantity rtq = null;
        for (int i = 0; i < days; i++) {
            rtq = findByRoomTypeAndQuantityDateForUpdate(gr.getRoomType(), startDate.plusDays(i));
            rtq.setUsedTotal(rtq.getUsedTotal() + 1);
            rtq.setPredictableTotal(rtq.getPredictableTotal() - 1);
            modify(rtq);
        }
        rtq = findByRoomTypeAndQuantityDateForUpdate(gr.getRoomType(), startDate.plusDays(days));
        rtq.setWillLeaveTotal(rtq.getWillLeaveTotal() + 1);
        modify(rtq);
    }

    @Override
    public List<RoomTypeQuantityPredictableVo> queryPredictable(String hotelCode, LocalDate startDate,
                                                                LocalDate endDate, String roomPriceSchemeId) {
        List<RoomType> types = roomTypeService.getAllByHotelCode(hotelCode, Constants.DELETED_FALSE);
        List<RoomTypeQuantityPredictableVo> data = new ArrayList<>();
        RoomTypeQuantityPredictableVo rtpv = null;
        for (RoomType type : types) {
            RoomTypeQuantity rtq = roomTypeQuantityDao.queryPredictable(type.getId(), startDate, endDate);
            if (rtq != null) {
                rtpv = new RoomTypeQuantityPredictableVo();
                if (roomPriceSchemeId != null && !"".equals(roomPriceSchemeId)) {
                    Map<String, Object> map = roomPriceSchemeDao.roomTypeAndPriceScheme(type.getId(), roomPriceSchemeId);
                    String id = MapUtils.getString(map, "id");
                    rtpv.setRoomPriceSchemeId(id);
                    rtpv.setSetMealId(MapUtils.getString(map, "setMealId"));
                    rtpv.setSetMealName(MapUtils.getString(map, "setMealName"));
                    rtpv.setPurchasePrice(MapUtils.getDouble(map, "price"));
                }
                rtpv.setRoomCode(type.getCode());
                rtpv.setPrice(type.getPrice());
                rtpv.setRoomTypeName(type.getName());
                rtpv.setRoomTypeId(type.getId());
                rtpv.setStartDate(startDate);
                rtpv.setEndDate(endDate);
                rtpv.setAvailableTotal(rtq.getPredictableTotal());
                rtpv.setOverReservation(type.getOverReservation());
                data.add(rtpv);
            }
        }
        return data;
    }

    @Override
    public List<RoomTypeQuantityPredictableVo> predictableWchat(String hotelCode, LocalDate startDate, LocalDate endDate) {
        List<RoomType> types = roomTypeService.getAllByHotelCode(hotelCode, Constants.DELETED_FALSE);
        List<RoomTypeQuantityPredictableVo> data = new ArrayList<>();
        RoomTypeQuantityPredictableVo rtpv = null;
        for (RoomType type : types) {
            RoomTypeQuantity rtq = roomTypeQuantityDao.queryPredictable(type.getId(), startDate, endDate);
            if (rtq != null) {
                List<Map<String, Object>> list = roomPriceSchemeDao.roomTypeAndPriceSchemeList(hotelCode, type.getId());
                rtpv = new RoomTypeQuantityPredictableVo();
                rtpv.setRoomCode(type.getCode());
                rtpv.setPrice(type.getPrice());
                rtpv.setRoomTypeName(type.getName());
                rtpv.setRoomTypeId(type.getId());
                rtpv.setStartDate(startDate);
                rtpv.setEndDate(endDate);
                rtpv.setAvailableTotal(rtq.getPredictableTotal());
                rtpv.setOverReservation(type.getOverReservation());
                rtpv.setRoomPriceSchemeList(list);
                data.add(rtpv);
            }
        }
        return data;
    }

    @Override
    public RoomTypeQuantityPredictableVo queryPredic(String currentHotleCode, String roomTypeId, LocalDate startDate,
                                                     LocalDate endDate) {
        RoomType roomType = roomTypeService.findById(roomTypeId);
        RoomTypeQuantityPredictableVo rtpv = null;
        if (roomType != null) {
            RoomTypeQuantity rtq = roomTypeQuantityDao.queryPredictable(roomTypeId, startDate, endDate);
            if (rtq != null) {
                rtpv = new RoomTypeQuantityPredictableVo();
                rtpv.setPrice(roomType.getPrice());
                rtpv.setRoomTypeName(roomType.getName());
                rtpv.setRoomTypeId(roomType.getId());
                rtpv.setStartDate(startDate);
                rtpv.setEndDate(endDate);
                rtpv.setAvailableTotal(rtq.getPredictableTotal());
            }
        }
        return rtpv;
    }

    @Override
    public List<RoomTypeQuantityVo> queryByDay(String currentHotleCode, LocalDate startDate, LocalDate endDate) {
        List<RoomTypeQuantityVo> data = new ArrayList<RoomTypeQuantityVo>();
        RoomTypeQuantityVo rv = null;
        for (RoomTypeQuantity r : roomTypeQuantityDao.queryByDay(currentHotleCode, startDate, endDate)) {
            rv = new RoomTypeQuantityVo();
            BeanUtils.copyProperties(r, rv);
            rv.setRoomTypeId(r.getRoomType().getId());
            rv.setRoomTypeName(r.getRoomType().getName());
            rv.setRoomTypeCode(r.getRoomType().getCode());
            data.add(rv);
        }
        return data;
    }

    @Override
    public boolean useRoomType(UseInfoAble info, String userType) {
       return  useRoomType(info.roomType(), info.getStartTime().toLocalDate(), info.getEndTime().toLocalDate(), userType,
                info.getRoomCount());
    }

    @Override
    public boolean useRoomType(UseInfoAble info, LocalDateTime extendTime) {
        RoomTypeQuantity rtq = null;
        LocalDate currentDate = info.getStartTime().toLocalDate();
        while (currentDate.isBefore(extendTime.toLocalDate())) {
            rtq = findByRoomTypeAndQuantityDateForUpdate(info.roomType(), currentDate);
            rtq.setPredictableTotal(rtq.getPredictableTotal() - 1);
            rtq.setBookingTotal(rtq.getBookingTotal() + 1);
            currentDate = currentDate.plusDays(1);
            modify(rtq);
        }
        return true;
    }

    @Override
    public List<RoomTypeQuantityVo> queryOneDay(String currentHotleCode, LocalDate date) {
        List<RoomTypeQuantityVo> data = new ArrayList<RoomTypeQuantityVo>();
        RoomTypeQuantityVo rv = null;
        for (RoomTypeQuantity r : roomTypeQuantityDao.findByHotelCodeAndQuantityDate(currentHotleCode, date)) {
            rv = new RoomTypeQuantityVo();
            BeanUtils.copyProperties(r, rv);
            rv.setRoomTypeId(r.getRoomType().getId());
            rv.setRoomTypeName(r.getRoomType().getName());
            rv.setRoomTypeCode(r.getRoomType().getCode());
            data.add(rv);
        }
        return data;
    }

    @Override
    public List<Map<String, Object>> getByTimeAndRoomType(String hotelCode, String times, String roomTypeId) {
        List<Map<String, Object>> list = roomPriceSchemeDao.getSql(hotelCode);
        List<Map<String, Object>> relist = new ArrayList<>();
//		List<Map<String, Object>> totle = roomTypeQuantityDao.getByTimeAndRoomType(hotelCode, times, roomTypeId);
        String schemeId = null;
        List<Map<String, Object>> schmes = roomPriceSchemeDao.getByRoomType(schemeId, roomTypeId);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> newMap = new HashMap<>();
            Map map = list.get(i);
            newMap.putAll(map);
            String fId = MapUtils.getString(map, "id");
            for (int j = 0; j < schmes.size(); j++) {
                String id = MapUtils.getString(schmes.get(j), "id");
                if (fId.equals(id)) {
                    String code = MapUtils.getString(schmes.get(j), "code");
                    String typeCode = MapUtils.getString(schmes.get(j), "typeCode");
                    String roomTypeName = MapUtils.getString(schmes.get(j), "roomTypeName");
                    String name = MapUtils.getString(schmes.get(j), "name");
                    String price = MapUtils.getString(schmes.get(j), "price");
                    String room_type_id = MapUtils.getString(schmes.get(j), "room_type_id");
                    String over_reservation = MapUtils.getString(schmes.get(j), "over_reservation");
                    newMap.put("over_" + typeCode, over_reservation);
                    newMap.put("price_" + typeCode, price);
                    newMap.put("typeName_" + typeCode, roomTypeName);
                    Map<String, Object> tMap = roomTypeQuantityDao.mapByTimeAndRoomType(hotelCode, times, room_type_id);
                    newMap.put("total_" + typeCode, MapUtils.getString(tMap, "predictable_total"));
                }
            }
            relist.add(newMap);
        }
        return relist;
    }

    @Override
    public List<Map<String, Object>> getByTimeAndRoomType2(String hotelCode, String startTimes, String[] buildIds,
                                                           String[] roomTypeIds) {
        List<String> bIds = null;
        if (buildIds != null) {
            bIds = Arrays.asList(buildIds);
        }
        List<String> rtIds = null;
        if (roomTypeIds != null) {
            rtIds = Arrays.asList(roomTypeIds);
        }
        List<Map<String, Object>> list = roomPriceSchemeDao.getSql(hotelCode);
        List<Map<String, Object>> relist = new ArrayList<>();
//		List<Map<String, Object>> totle = roomTypeQuantityDao.getByTimeAndRoomType(hotelCode, times, roomTypeId);
        String schemeId = null;
        List<Map<String, Object>> schmes = roomPriceSchemeDao.getByRoomType2(schemeId, rtIds);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> newMap = new HashMap<>();
            Map map = list.get(i);
            newMap.putAll(map);
            String fId = MapUtils.getString(map, "id");
            for (int j = 0; j < schmes.size(); j++) {
                String id = MapUtils.getString(schmes.get(j), "id");
                if (fId.equals(id)) {
//                    String code = MapUtils.getString(schmes.get(j), "code");
                    String typeCode = MapUtils.getString(schmes.get(j), "typeCode");
                    String roomTypeName = MapUtils.getString(schmes.get(j), "roomTypeName");
//                    String name = MapUtils.getString(schmes.get(j), "name");
                    String price = MapUtils.getString(schmes.get(j), "price");
                    String room_type_id = MapUtils.getString(schmes.get(j), "room_type_id");
                    String over_reservation = MapUtils.getString(schmes.get(j), "over_reservation");
                    newMap.put("over_" + typeCode, over_reservation);
                    newMap.put("price_" + typeCode, price);
                    newMap.put("typeName_" + typeCode, roomTypeName);
                    Map<String, Object> tMap = roomTypeQuantityDao.mapByTimeAndRoomType(hotelCode, startTimes, room_type_id);
//                    Map<String, Object> tMap = roomTypeQuantityDao.mapByTimeAndRoomType2(hotelCode, startTimes, endTime, bIds,
//                            room_type_id);
                    if (tMap == null || tMap.isEmpty()) {
                        newMap.put("total_" + typeCode, 0);
                    } else {
//                        newMap.put("total_" + typeCode, MapUtils.getString(tMap, "total"));
                        newMap.put("total_" + typeCode, MapUtils.getString(tMap, "predictable_total"));
                    }
                }
            }
            relist.add(newMap);
        }
        return relist;
    }

    @Override
    public boolean changeRoom(RoomType roomType, RoomType newRoomType, String status, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime changeTime) {
        if (status.equals(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION)) {
            status = Constants.Status.ROOM_USAGE_ASSIGN;
        }
        changeRoomTypeQuantity(roomType, changeTime.toLocalDate(), endTime.toLocalDate(), status, Constants.Status.ROOM_USAGE_FREE, 1);
        changeRoomTypeQuantity(newRoomType, changeTime.toLocalDate(), endTime.toLocalDate(), Constants.Status.ROOM_USAGE_FREE, status, 1);
        return true;
    }

    @Override
    public boolean extendTime(RoomType roomType, String roomStatus, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime newStartTime, LocalDateTime newEndTime, int quantity) {
        changeRoomTypeQuantity(roomType, startTime.toLocalDate(), endTime.toLocalDate(), roomStatus, Constants.Status.ROOM_USAGE_FREE, 1,!roomStatus.equals(Constants.Status.ROOM_USAGE_CHECK_IN));
        LocalDate newStartDate = null;
        LocalDate newEndDate = null;
        if (newStartTime != null) {
            newStartDate = newStartTime.toLocalDate();
        } else {
            newStartDate = startTime.toLocalDate();
        }
        if (newEndTime != null) {
            newEndDate = newEndTime.toLocalDate();
        } else {
            newEndDate = endTime.toLocalDate();
        }
        changeRoomTypeQuantity(roomType, newStartDate, newEndDate, Constants.Status.ROOM_USAGE_FREE, roomStatus, 1,!roomStatus.equals(Constants.Status.ROOM_USAGE_CHECK_IN));
        return true;
    }


    @Override
    public void initNewType(RoomType roomType) {
        LocalDate planDate = businessSeqService.getPlanDate(roomType.getHotelCode());
        LocalDate quantityDate = LocalDate.now();
        while (planDate.isAfter(quantityDate)) {
            entityManager.persist(initRoomTypeQuantity(roomType, quantityDate));
            quantityDate = quantityDate.plusDays(1);
        }
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public void addRoomQuantity(RoomType roomType, int quantity) {
        roomTypeQuantityDao.plusRoomTypeQuantity(roomType.getId(), quantity);
    }
}
