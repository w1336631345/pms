package com.kry.pms.service.report;

import com.kry.pms.model.http.response.report.CommomReportTableData;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Map;

public interface CommomReportService {
    public CommomReportTableData fetchCommonReport(String id,Map<String, Object> params) throws IOException, TemplateException;
}
