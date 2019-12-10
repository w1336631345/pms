package com.kry.pms.service.report;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.FrontEntryReport;
import com.kry.pms.model.persistence.report.FrontReceiveReport;
import com.kry.pms.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface FrontReceiveReportService extends BaseService<FrontReceiveReport> {

    HttpResponse receiveList(String hotelCode, String businessDate);

    List<Map<String, Object>> getList(String hotelCode, String businessDate, String empName);
}
