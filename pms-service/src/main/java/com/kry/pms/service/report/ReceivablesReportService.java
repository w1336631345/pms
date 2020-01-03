package com.kry.pms.service.report;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.ReceivablesReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

import java.time.LocalDate;
import java.util.List;

public interface ReceivablesReportService extends BaseService<ReceivablesReport> {


    List<ReceivablesReport> findByHotelCodeAndBusinessDate(String hotelCode, LocalDate businessDate);

    HttpResponse totalByTypeName(String hotelCode, String businessDate);
}
