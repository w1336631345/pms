package com.kry.pms.service.busi;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.busi.RoomChangeRecord;
import com.kry.pms.service.BaseService;

import javax.transaction.Transactional;

public interface RoomChangeRecordService extends BaseService<RoomChangeRecord> {


    HttpResponse save(RoomChangeRecord entity);

    @Transactional
    HttpResponse saveOld(String hotelCode, RoomChangeRecord entity);
}