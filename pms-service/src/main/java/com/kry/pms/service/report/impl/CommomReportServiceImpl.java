package com.kry.pms.service.report.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kry.pms.model.http.response.report.CommomReportTableData;
import com.kry.pms.model.http.response.report.ReportTableColumn;
import com.kry.pms.model.persistence.report.ReportTableDefinition;
import com.kry.pms.service.report.CommomReportService;
import com.kry.pms.service.report.ReportTableDefinitionService;
import com.kry.pms.service.sys.SqlTemplateService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CommomReportServiceImpl implements CommomReportService {
    @Autowired
    ReportTableDefinitionService reportTableDefinitionService;
    @Autowired
    SqlTemplateService sqlTemplateService;

    @Override
    public CommomReportTableData fetchCommonReport(String id, Map<String, String[]> params) throws IOException, TemplateException {
        CommomReportTableData data = new CommomReportTableData();
        ReportTableDefinition rd = reportTableDefinitionService.findById(id);
        if (rd != null) {
            data.setColumns(createColumns(rd));
            data.setData(sqlTemplateService.processTemplateQuery(rd.getGroupKey()+rd.getCode(),rd.getDataValue(),params));
            data.setName(rd.getName());
        }
        return data;
    }

    private List<ReportTableColumn> createColumns(ReportTableDefinition rd) {
        Gson gson = new Gson();
        return gson.fromJson(rd.getHeaderValue(), new TypeToken<List<ReportTableColumn>>() {
        }.getType());
    }
}
