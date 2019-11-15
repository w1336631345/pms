package com.kry.pms.service.report;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.report.RoomReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

import java.util.List;
import java.util.Map;

public interface RoomReportService extends BaseService<RoomReport> {

    HttpResponse copyData(String businessDate);

    List<RoomReport> getByHotelCodeAndBusinessDate(User user, String businessDate);

    List<Map<String, Object>> getRoomStatus(User user, String businessDate);
}
