package com.kry.pms.api.report;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.report.BusinessReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/report/businessReport")
public class BusinessReportController extends BaseController {

    @Autowired
    BusinessReportService businessReportService;

    @GetMapping("/save")
    public HttpResponse auditNight(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        businessReportService.saveReport(user, null, businessDate);
        return hr;
    }

    @GetMapping("/report")
    public HttpResponse getByBusinessDate(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        List<BusinessReport> list = businessReportService.getByBusinessDate(user, null);
        hr.setData(list);
        return hr;
    }

}
