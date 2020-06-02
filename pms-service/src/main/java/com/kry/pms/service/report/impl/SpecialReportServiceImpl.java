package com.kry.pms.service.report.impl;

import com.kry.pms.dao.report.ReportEmpDailyBillStatDao;
import com.kry.pms.model.persistence.report.ReportEmpDailyBillStat;
import com.kry.pms.service.report.SpecialReportService;
import com.kry.pms.service.sys.SqlTemplateService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecialReportServiceImpl implements SpecialReportService {
    @Autowired
    SqlTemplateService sqlTemplateService;
    @Autowired
    ReportEmpDailyBillStatDao reportEmpDailyBillStatDao;

    @Override
    public List<Map<String, Object>> billCostStat(String hotelCode, String employeeId, String shift, LocalDate businessDate) throws IOException, TemplateException {
        Map<String, Object> parmrs = new HashMap<>();
        parmrs.put("hotelCode", hotelCode);
        parmrs.put("employee_id", employeeId);
        parmrs.put("shift", shift);
        parmrs.put("business_date", businessDate);
        return sqlTemplateService.processByCode(hotelCode, "report_cost_stat", parmrs);
    }

    @Override
    public List<Map<String, Object>> billPayStat(String hotelCode, String employeeId, String shift, LocalDate businessDate) throws IOException, TemplateException {
        Map<String, Object> parmrs = new HashMap<>();
        parmrs.put("hotelCode", hotelCode);
        parmrs.put("employee_id", employeeId);
        parmrs.put("shift", shift);
        parmrs.put("business_date", businessDate);
        return sqlTemplateService.processByCode(hotelCode, "report_pay_stat", parmrs);
    }

    @Override
    public Collection<Map<String, Object>> dailyReport(String hotelCode, LocalDate bDate) throws IOException, TemplateException {
        List<ReportEmpDailyBillStat> data = reportEmpDailyBillStatDao.findByHotelCodeAndQuantityDate(hotelCode, bDate);
        return rowToCol(data);
    }

    @Override
    public Collection<Map<String, Object>> paySummary(String hotelCode, LocalDate bDate) throws IOException, TemplateException {
        List<ReportEmpDailyBillStat> data = reportEmpDailyBillStatDao.findByHotelCodeAndQuantityDate(hotelCode, bDate);
        return rowToCol(data);
    }

    private Collection<Map<String, Object>> rowToCol(List<ReportEmpDailyBillStat> data) {
        HashMap<String, Map<String, Object>> rep = new HashMap<>();
        for (ReportEmpDailyBillStat item : data) {
            if (!rep.containsKey(item.getEmployeeId())) {
                rep.put(item.getEmployeeId(), new HashMap<>());
            }
            rep.get(item.getEmployeeId()).put(item.getKey(), item.getValue());
        }
        return rep.values();
    }
}
