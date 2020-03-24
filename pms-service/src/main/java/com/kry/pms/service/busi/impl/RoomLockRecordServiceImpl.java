package com.kry.pms.service.busi.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.kry.pms.service.room.RoomStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.RoomLockRecordDao;
import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.busi.RoomLockRecordService;

import javax.annotation.Resource;

@Service
public class RoomLockRecordServiceImpl implements RoomLockRecordService {
    @Resource
    RoomLockRecordDao roomLockRecordDao;
    @Autowired
    RoomStatisticsService roomStatisticsService;


    @Override
    public RoomLockRecord add(RoomLockRecord roomLockRecord) {
        return roomLockRecordDao.saveAndFlush(roomLockRecord);
    }

    @Override
    public void delete(String id) {
        RoomLockRecord roomLockRecord = roomLockRecordDao.findById(id).get();
        if (roomLockRecord != null) {
            roomLockRecord.setDeleted(Constants.DELETED_TRUE);
        }
        modify(roomLockRecord);
    }

    @Override
    public RoomLockRecord modify(RoomLockRecord roomLockRecord) {
        return roomLockRecordDao.saveAndFlush(roomLockRecord);
    }

    @Override
    public RoomLockRecord findById(String id) {
        return roomLockRecordDao.getOne(id);
    }

    @Override
    public List<RoomLockRecord> getAllByHotelCode(String code) {
        return null;// 默认不实现
        // return roomLockRecordDao.findByHotelCode(code);
    }

    @Override
    public PageResponse<RoomLockRecord> listPage(PageRequest<RoomLockRecord> prq) {
        Example<RoomLockRecord> ex = Example.of(prq.getExb());
        org.springframework.data.domain.PageRequest req;
        if (prq.getOrderBy() != null) {
            Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
        } else {
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
        }
        return convent(roomLockRecordDao.findAll(ex, req));
    }

    private RoomLockRecord createRecord(GuestRoom gr, LocalDateTime startTime, LocalDateTime endTime,
                                       String reason, String endToStatus,String type) {
        RoomLockRecord rlr = new RoomLockRecord();
        rlr.setGuestRoom(gr);
        rlr.setHotelCode(gr.getHotelCode());
        rlr.setStatus(Constants.Status.NORMAL);
        rlr.setStartTime(startTime);
        rlr.setEndTime(endTime);
        rlr.setReason(reason);
        rlr.setEndToStatus(endToStatus);
        rlr.setType(type);
        return rlr;
    }

    @Override
    public RoomLockRecord openLock(String id, String operationEmployeeId) {
        RoomLockRecord rlr = findById(id);
        if (rlr != null && rlr.getStatus().equals(Constants.Status.NORMAL)) {
            roomStatisticsService.cancleLock(rlr, LocalDateTime.now());
            rlr.setStatus(Constants.Status.CLOSE);
            modify(rlr);
        }
        return rlr;
    }

    @Override
    public List<RoomLockRecord> findByGuestRoomAndStatus(GuestRoom guestRoom, String status) {
        return roomLockRecordDao.findByGuestRoomAndStatus(guestRoom, status);
    }

    @Override
    public RoomLockRecord lockRoom(GuestRoom gr, LocalDateTime startTime, LocalDateTime endTime, String reasonId, String endToStatus,String type) {
        RoomLockRecord rlr = createRecord(gr, startTime, endTime, reasonId, endToStatus,type);
        rlr = add(rlr);
        roomStatisticsService.lock(rlr);
        return rlr;
    }

}
