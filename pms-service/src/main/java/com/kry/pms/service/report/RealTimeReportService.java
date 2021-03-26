package com.kry.pms.service.report;

import com.kry.pms.base.HttpResponse;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RealTimeReportService {


    List<Map<String, Object>> billCostStat(String hotelCode, String employeeId, String shift, LocalDate businessDate) throws IOException, TemplateException;

    List<Map<String, Object>> billPayStat(String hotelCode, String employeeId, String shift, LocalDate businessDate) throws IOException, TemplateException;

    List<Map<String, Object>> sectionDailyReport(String hotelCode, LocalDate parse, LocalDate parse1) throws IOException, TemplateException;

    HttpResponse<Object> reCalculation(String hotelCode, LocalDate quantityDate, String calcType);
}
