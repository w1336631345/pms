package com.kry.pms.api.report;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.report.BusinessReportService;
import com.kry.pms.service.sys.BusinessSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/report/businessReport")
public class BusinessReportController extends BaseController {

    @Autowired
    BusinessReportService businessReportService;
    @Autowired
    BusinessSeqService businessSeqService;

    /**
     * 功能描述: <br>获取当前营业日期
     * 〈〉
     * @Param: []
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/27 10:47
     */
    @GetMapping("/businessDate")
    public HttpResponse businessDate(){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
       LocalDate businessDate = businessSeqService.getBuinessDate(user.getHotelCode());
        hr.addData(businessDate);
        return hr;
    }

    /**
     * 功能描述: <br>保存营业日报表，每日夜审之前保存一次
     * 〈〉
     * @Param: [businessDate]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/19 10:54
     */
    @GetMapping("/save")
    public HttpResponse auditNight(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        hr = businessReportService.saveReport(user, null, businessDate);
        return hr;
    }

    /**
     * 功能描述: <br>查询营业日报表列表
     * 〈〉
     * @Param: [businessDate]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/19 10:55
     */
    @GetMapping("/report")
    public HttpResponse getByBusinessDate(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        List<BusinessReport> list = businessReportService.getByBusinessDate(user, businessDate);
        hr.setData(list);
        return hr;
    }

    @GetMapping("/costByGroupType")
    public HttpResponse costByGroupType(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        hr = businessReportService.costByGroupType(user, businessDate);
        return hr;
    }

}
