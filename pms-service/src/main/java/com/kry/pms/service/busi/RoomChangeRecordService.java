package com.kry.pms.service.busi;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.busi.RoomChangeRecord;
import com.kry.pms.service.BaseService;

public interface RoomChangeRecordService extends BaseService<RoomChangeRecord> {


    HttpResponse save(String hotelCode, RoomChangeRecord entity);
}