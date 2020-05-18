package com.kry.pms.api.report;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.service.report.RealTimeReportService;
import com.kry.pms.service.sys.BusinessSeqService;
import freemarker.template.TemplateException;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
    public HttpResponse<List<Map<String, Object>>> realTimeShiftCost() throws IOException, TemplateException {
        HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<>();
        String hotelCode = getCurrentHotleCode();
        rep.addData(realTimeReportService.billCostStat(getCurrentHotleCode(), getCurrentEmployee().getId(), getShiftCode(), businessSeqService.getBuinessDate(hotelCode)));
        return rep;
    }

    @GetMapping
    @RequestMapping("/shift/pay")
    public HttpResponse<List<Map<String, Object>>> realTimeShiftPay() throws IOException, TemplateException {
        HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<>();
        String hotelCode = getCurrentHotleCode();
        rep.addData(realTimeReportService.billPayStat(getCurrentHotleCode(), getCurrentEmployee().getId(), getShiftCode(), businessSeqService.getBuinessDate(hotelCode)));
        return rep;
    }
}
