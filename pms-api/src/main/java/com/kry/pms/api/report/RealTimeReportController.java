package com.kry.pms.api.report;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.service.report.RealTimeReportService;
import com.kry.pms.service.sys.BusinessSeqService;
import freemarker.template.TemplateException;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/report/real")
public class RealTimeReportController extends BaseController {
    @Autowired
    RealTimeReportService realTimeReportService;
    @Autowired
    BusinessSeqService businessSeqService;

    @GetMapping
    @RequestMapping("/shift/cost")
    public HttpResponse<List<Map<String, Object>>> realTimeShiftCost( String employee_id,  String shift) throws IOException, TemplateException {
        HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<>();
        String hotelCode = getCurrentHotleCode();
        rep.addData(realTimeReportService.billCostStat(getCurrentHotleCode(), employee_id, shift, businessSeqService.getBuinessDate(hotelCode)));
        return rep;
    }

    @GetMapping
    @RequestMapping("/shift/pay")
    public HttpResponse<List<Map<String, Object>>> realTimeShiftPay( String employee_id,  String shift) throws IOException, TemplateException {
        HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<>();
        String hotelCode = getCurrentHotleCode();
        rep.addData(realTimeReportService.billPayStat(getCurrentHotleCode(), employee_id, shift, businessSeqService.getBuinessDate(hotelCode)));
        return rep;
    }

    @GetMapping
    @RequestMapping("/section/daily")
    public HttpResponse<List<Map<String, Object>>> sectionDailyReport(String startDate, String endDate) throws IOException, TemplateException {
        HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<>();
        String hotelCode = getCurrentHotleCode();
        rep.addData(realTimeReportService.sectionDailyReport(hotelCode, LocalDate.parse(startDate), LocalDate.parse(endDate)));
        return rep;
    }

    @PostMapping
    @RequestMapping("/section/reCalculation")
    public HttpResponse<Object> reCalculation(String hotelCode, String editDate, String calcType) {
        HttpResponse<Object> response = new HttpResponse<>();
        System.out.println("type ==> " + calcType);
        System.out.println("hotelCode ==> " + hotelCode);
        System.out.println("editDate ==> " + editDate);
        LocalDate quantityDate = LocalDate.parse(editDate);
        return  response.addData(realTimeReportService.reCalculation(hotelCode, quantityDate, calcType));
    }
}
