package com.kry.pms.service.audit.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.service.audit.NightAuditService;

import java.util.List;

public class NightAuditServiceImpl implements NightAuditService {

    @Override
    public RoomRecord add(RoomRecord entity) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public RoomRecord modify(RoomRecord roomRecord) {
        return null;
    }

    @Override
    public RoomRecord findById(String id) {
        return null;
    }

    @Override
    public List<RoomRecord> getAllByHotelCode(String code) {
        return null;
    }

    @Override
    public PageResponse<RoomRecord> listPage(PageRequest<RoomRecord> prq) {
        return null;
    }
}
