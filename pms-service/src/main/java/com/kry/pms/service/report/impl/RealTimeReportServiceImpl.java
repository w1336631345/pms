package com.kry.pms.service.report.impl;

import com.kry.pms.service.report.RealTimeReportService;
import com.kry.pms.service.sys.SqlTemplateService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RealTimeReportServiceImpl implements RealTimeReportService {

    @Autowired
    SqlTemplateService sqlTemplateService;
    @Override
    public List<Map<String, Object>> billCostStat(String hotelCode, String employeeId, String shift, LocalDate businessDate) throws IOException, TemplateException {
        Map<String, Object> parmrs = new HashMap<>();
        parmrs.put("hotelCode", hotelCode);
        parmrs.put("employee_id", employeeId);
        parmrs.put("shift", shift);
        parmrs.put("business_date", businessDate);
        return sqlTemplateService.processByCode(hotelCode, "realtime_cost_stat", parmrs);
    }

    @Override
    public List<Map<String, Object>> billPayStat(String hotelCode, String employeeId, String shift, LocalDate businessDate) throws IOException, TemplateException {
        Map<String, Object> parmrs = new HashMap<>();
        parmrs.put("hotelCode", hotelCode);
        parmrs.put("employee_id", employeeId);
        parmrs.put("shift", shift);
        parmrs.put("business_date", businessDate);
        return sqlTemplateService.processByCode(hotelCode, "realtime_pay_stat", parmrs);
    }

}
