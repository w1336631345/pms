package com.kry.pms.api.report;

import com.kry.pms.base.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/report/real")
public class RealTimeReportController {

    @GetMapping
    @RequestMapping("/shift/cost")
    public HttpResponse<Map<String,Object>>  realTimeShiftCost(){
        HttpResponse<Map<String,Object>> rep = new HttpResponse<>();
        return rep;
    }
    @GetMapping
    @RequestMapping("/shift/pay")
    public HttpResponse<Map<String,Object>>  realTimeShiftPay(){
        HttpResponse<Map<String,Object>> rep = new HttpResponse<>();
        return rep;
    }
}
