package com.kry.pms.api.report;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.ReceivablesReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.report.ReceivablesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/report/receivables")
public class ReceivablesReportController extends BaseController {

    @Autowired
    ReceivablesReportService receivablesReportService;

    @GetMapping("/totalByTypeName")
    public HttpResponse totalByTypeName(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        hr = receivablesReportService.totalByTypeName(user.getHotelCode(), businessDate);
        return hr.ok();
    }

    /**
     * 功能描述: <br>查询收款汇总列表
     * 〈〉
     * @Param: [businessDate]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/4 18:13
     */
    @GetMapping("/getList")
    public HttpResponse getList(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        List<ReceivablesReport> list = receivablesReportService.findByHotelCodeAndBusinessDate(user.getHotelCode(), LocalDate.parse(businessDate));
        hr.setData(list);
        return hr;
    }
}
