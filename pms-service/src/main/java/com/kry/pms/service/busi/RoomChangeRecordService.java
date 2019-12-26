package com.kry.pms.service.busi;

import com.kry.pms.model.persistence.busi.RoomChangeRecord;
import com.kry.pms.service.BaseService;

public interface RoomChangeRecordService extends BaseService<RoomChangeRecord> {


    RoomChangeRecord save(String hotelCode, RoomChangeRecord entity);
}