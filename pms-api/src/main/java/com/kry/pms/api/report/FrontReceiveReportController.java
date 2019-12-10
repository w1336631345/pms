package com.kry.pms.api.report;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.FrontEntryReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.report.FrontEntryReportService;
import com.kry.pms.service.report.FrontReceiveReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/report/frontReceive")
public class FrontReceiveReportController extends BaseController {

    @Autowired
    FrontReceiveReportService frontReceiveReportService;

    /**
     * 功能描述:生成前台收款报表
     * 〈〉
     * @Param: [businessDate]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/4 18:13
     */
    @GetMapping("/receiveList")
    public HttpResponse receiveList(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        hr = frontReceiveReportService.receiveList(user.getHotelCode(), businessDate);
        return hr;
    }

    /**
     * 功能描述: <br>查询前台收款报表
     * 〈〉
     * @Param: [businessDate, cashier]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/7 17:37
     */
    @GetMapping("/getReceiveList")
    public HttpResponse getReceiveList(String businessDate, String cashier){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        List<Map<String, Object>> list = frontReceiveReportService.getList(user.getHotelCode(), businessDate, cashier);
        hr.setData(list);
        return hr;
    }
}
