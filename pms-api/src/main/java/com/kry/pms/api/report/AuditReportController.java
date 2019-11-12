package com.kry.pms.api.report;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.report.AuditReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/report/auditReport")
public class AuditReportController extends BaseController {
    @Autowired
    AuditReportService auditReportService;

    /**
     * 功能描述: <br>夜间稽核工作底表-营业收入
     * 〈〉
     * @Param: [businessDate]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/12 16:22
     */
    @GetMapping("/auditNight")
    public HttpResponse auditNight(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        List<Map<String, Object>> list = auditReportService.auditNight(user, null);
        hr.setData(list);
        return hr;
    }

    /**
     * 功能描述: <br>夜间稽核工作底表-收款记录
     * 〈〉
     * @Param: [businessDate]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/12 16:22
     */
    @GetMapping("/receivables")
    public HttpResponse receivables(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        List<Map<String, Object>> list = auditReportService.receivables(user, null);
        hr.setData(list);
        return hr;
    }
}
