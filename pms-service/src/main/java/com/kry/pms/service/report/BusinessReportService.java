package com.kry.pms.service.report;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface BusinessReportService extends BaseService<BusinessReport> {

    HttpResponse saveReportAll(User user, String projectType, String businessDate);

    HttpResponse saveReport(String hotelCode, String projectType, String businessDate);

    List<BusinessReport> getByBusinessDate(User user, String businessDate);

    HttpResponse costByGroupType(String hotelCode, String businessDate);

    //稽核报表-营业日报表-e、平均房价
    HttpResponse costByGroupTypeAvg(String hotelCode, String businessDate);
}
