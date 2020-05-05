package com.kry.pms.service.report.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kry.pms.base.Constants;
import com.kry.pms.model.http.response.report.CommomReportTableData;
import com.kry.pms.model.http.response.report.ReportTableColumn;
import com.kry.pms.model.persistence.report.ReportTableDefinition;
import com.kry.pms.service.report.CommomReportService;
import com.kry.pms.service.report.ReportTableDefinitionService;
import com.kry.pms.service.sys.SqlTemplateService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommomReportServiceImpl implements CommomReportService {
    @Autowired
    ReportTableDefinitionService reportTableDefinitionService;
    @Autowired
    SqlTemplateService sqlTemplateService;
    @Autowired
    Configuration configuration;

    @Override
    public CommomReportTableData fetchCommonReport(String id, Map<String, String[]> params) throws IOException, TemplateException {
        ReportTableDefinition rd = reportTableDefinitionService.findById(id);
        CommomReportTableData data = new CommomReportTableData();
        if (rd != null) {
            data.setType(rd.getType());
            if(rd.getType().equals(Constants.Type.REPORT_TABLE_DEF_TABLE)){
                data.setColumns(createColumns(rd));
                data.setData(sqlTemplateService.processTemplateQuery(rd.getGroupKey() + rd.getCode(), rd.getDataValue(), params));
                data.setName(rd.getName());
            }else if(rd.getType().equals(Constants.Type.REPORT_TABLE_DEF_TEMPLATE)){
                data = fetchTemplate(rd,params);
            }
        }
        return data;
    }

    private CommomReportTableData fetchTemplate(ReportTableDefinition rd, Map<String, String[]> params) throws IOException, TemplateException {
        CommomReportTableData rep = new CommomReportTableData();
        rep.setType(rd.getType());
        List<Map<String, Object>> data = sqlTemplateService.processTemplateQuery(rd.getGroupKey() + rd.getCode(), rd.getDataValue(), params);
        Map<String, Object> webData = new HashMap<>();
        if (data != null && !data.isEmpty()) {
            for (Map<String, Object> item : data) {
                webData.put(item.get("key").toString(), item.get("value_").toString());
            }
        }
        Template template = null;
        template = new Template(rd.getHotelCode(), rd.getWebTemplate(), configuration);
        StringWriter stringWriter = new StringWriter();
        template.process(webData, stringWriter);
        String html = stringWriter.toString();
        stringWriter.close();
        rep.setHtml(html);
        return rep;

    }

    private List<ReportTableColumn> createColumns(ReportTableDefinition rd) {
        Gson gson = new Gson();
        return gson.fromJson(rd.getHeaderValue(), new TypeToken<List<ReportTableColumn>>() {
        }.getType());
    }
}
