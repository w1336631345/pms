package com.kry.pms.api.report;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.FrontEntryReport;
import com.kry.pms.model.persistence.report.ReceivablesReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.report.FrontEntryReportService;
import com.kry.pms.service.report.ReceivablesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/report/frontEntry")
public class FrontEntryReportController extends BaseController {

    @Autowired
    FrontEntryReportService frontEntryReportService;

    /**
     * 功能描述: <br>根据营业日期，生成前台入账报表
     * 〈〉
     * @Param: [businessDate]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/5 17:43
     */
    @GetMapping("/total")
    public HttpResponse total(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        hr = frontEntryReportService.frontEntryList2(user.getHotelCode(), businessDate);
        return hr.ok();
    }

    /**
     * 功能描述:查询前台入账列表(弃用)
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
        List<FrontEntryReport> list = frontEntryReportService.findByHotelCodeAndBusinessDate(user.getHotelCode(), businessDate);
        hr.setData(list);
        return hr;
    }

    @GetMapping("/getFrontList")
    public HttpResponse getFrontList(String businessDate, String cashier){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        List<FrontEntryReport> list = frontEntryReportService.getFrontEntryList(user.getHotelCode(), businessDate, cashier);
        hr.setData(list);
        return hr;
    }
}
