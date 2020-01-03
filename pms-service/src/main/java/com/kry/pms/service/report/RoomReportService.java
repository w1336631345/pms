package com.kry.pms.service.report;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.report.RoomReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RoomReportService extends BaseService<RoomReport> {

    HttpResponse copyData(String businessDate);

    List<RoomReport> getByHotelCodeAndBusinessDate(User user, LocalDate businessDate);

    HttpResponse saveRoomStatus(User user, String businessDate);

    HttpResponse totalRoomStatusAll(String hotelCode, String businessDate);

    HttpResponse totalCheckInType(String hotelCode, String businessDate);

    @Transactional(rollbackFor=Exception.class)
    HttpResponse availableTotal(String hotelCode, String businessDate);
}
