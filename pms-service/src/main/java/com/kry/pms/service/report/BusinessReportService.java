package com.kry.pms.service.report;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface BusinessReportService extends BaseService<BusinessReport> {

    HttpResponse saveReport(User user, String projectType, String businessDate);

    List<BusinessReport> getByBusinessDate(User user, String businessDate);
}
