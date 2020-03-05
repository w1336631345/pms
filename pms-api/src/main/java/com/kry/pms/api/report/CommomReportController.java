package com.kry.pms.api.report;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.response.report.CommomReportTableData;
import com.kry.pms.service.report.CommomReportService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api/v1/report/commomReport")
public class CommomReportController {
    @Autowired
    CommomReportService commomReportService;
    @GetMapping("/{id}")
    public HttpResponse<CommomReportTableData> fetchData(@PathVariable("id") String id, HttpServletRequest request) throws IOException, TemplateException {
        HttpResponse<CommomReportTableData> rep = new HttpResponse<>();
        rep.setData(commomReportService.fetchCommonReport(id, request.getParameterMap()));
        return rep;
    }

}
