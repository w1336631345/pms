package com.kry.pms.api.audit;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.audit.AuditNightStepParamService;
import com.kry.pms.service.report.AuditReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/report/auditNightStepParam")
public class AuditNightStepParamController extends BaseController {
    @Autowired
    AuditNightStepParamService auditNightStepParamService;

    /**
     * 功能描述: <br>
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
        List<Map<String, Object>> list = new ArrayList<>();
        hr.setData(list);
        return hr;
    }
}
