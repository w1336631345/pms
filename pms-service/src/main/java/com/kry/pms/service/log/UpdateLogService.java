package com.kry.pms.service.log;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.log.UpdateLog;
import com.kry.pms.model.persistence.room.GuestRoomStatus;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface UpdateLogService extends BaseService<UpdateLog>{


    List<UpdateLog> findByHotelCodeAndProductType(String code, String type);

    HttpResponse updateCirAllLog(CheckInRecord checkInRecord);

    GuestRoomStatus guestRoomStatusModify(GuestRoomStatus guestRoomStatus);
}