package com.kry.pms.service.report;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.FrontEntryReport;
import com.kry.pms.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FrontEntryReportService extends BaseService<FrontEntryReport> {

    List<FrontEntryReport> findByHotelCodeAndBusinessDate(String hotelCode, String businessDate);

    HttpResponse frontEntryList(String hotelCode, String businessDate);

    //自动夜审生成报表，前台入账报表（方案2）
    @Transactional(rollbackFor=Exception.class)
    HttpResponse frontEntryList2(String hotelCode, String businessDate);

    List<FrontEntryReport> getFrontEntryList(String hotelCode, String businessDate, String cashier);
}
