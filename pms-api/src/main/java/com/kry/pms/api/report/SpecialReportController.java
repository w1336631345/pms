package com.kry.pms.api.report;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.service.report.SpecialReportService;
import com.kry.pms.service.report.impl.SpecialReportServiceImpl;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/report/special")
public class SpecialReportController extends BaseController {
    @Autowired
    SpecialReportService specialReportService;

    @GetMapping
    @RequestMapping("/cost")
    public HttpResponse<List<Map<String, Object>>> cost( String employee_id, @RequestParam(required = true) String business_date, String shift) throws IOException, TemplateException {
        HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<>();
        LocalDate localDate = LocalDate.parse(business_date);
        rep.addData(specialReportService.billCostStat(getCurrentHotleCode(), employee_id, shift, localDate));
        return rep;
    }

    @GetMapping
    @RequestMapping("/pay")
    public HttpResponse<List<Map<String, Object>>> pay( String employee_id, @RequestParam(required = true) String business_date, String shift) throws IOException, TemplateException {
        HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<>();
        LocalDate localDate = LocalDate.parse(business_date);
        rep.addData(specialReportService.billPayStat(getCurrentHotleCode(), employee_id, shift, localDate));
        return rep;
    }
    @GetMapping
    @RequestMapping("/dailyReport")
    public HttpResponse<Collection<Map<String, Object>>> dailyReport(@RequestParam(required = true) String business_date) throws IOException, TemplateException {
        HttpResponse<Collection<Map<String, Object>>> rep = new HttpResponse<>();
        LocalDate bDate = LocalDate.parse(business_date);
        rep.addData(specialReportService.dailyReport(getCurrentHotleCode(),bDate));
        return rep;
    }
    @GetMapping
    @RequestMapping("/paySummary")
    public HttpResponse<Collection<Map<String, Object>>> paySummary(@RequestParam(required = true) String business_date) throws IOException, TemplateException {
        HttpResponse<Collection<Map<String, Object>>> rep = new HttpResponse<>();
        LocalDate bDate = LocalDate.parse(business_date);
        rep.addData(specialReportService.paySummary(getCurrentHotleCode(),bDate));
        return rep;
    }

}
