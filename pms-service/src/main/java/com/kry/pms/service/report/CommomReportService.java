package com.kry.pms.service.report;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.response.report.CommomReportTableData;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public interface CommomReportService {
    public CommomReportTableData fetchCommonReport(String id,Map<String, Object> params) throws IOException, TemplateException;

    //重算报表
    HttpResponse resetProcedure(String procedureName, String hotelCode, LocalDate date);
}
