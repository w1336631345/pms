package com.kry.pms.api.report;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.report.RoomReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.report.BusinessReportService;
import com.kry.pms.service.report.RoomReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/report/roomReport")
public class RoomReportController extends BaseController {

    @Autowired
    RoomReportService roomReportService;

    @GetMapping("/copyData")
    public HttpResponse auditNight(String businessDate){
        HttpResponse hr = new HttpResponse();
        roomReportService.copyData(businessDate);
        return hr;
    }

    @GetMapping("/getRoomStatus")
    public HttpResponse getRoomStatus(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        List<Map<String, Object>> list = roomReportService.getRoomStatus(user, businessDate);
        hr.setData(list);
        return hr;
    }

}
