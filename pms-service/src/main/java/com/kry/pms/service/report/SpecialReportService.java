package com.kry.pms.service.report;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SpecialReportService {

    List<Map<String, Object>> billCostStat(String hotelCode, String employeeId, String shift, LocalDate startDate,LocalDate endDate) throws IOException, TemplateException;

    List<Map<String, Object>> billPayStat(String hotelCode, String employeeId, String shift,LocalDate startDate,LocalDate endDate) throws IOException, TemplateException;

    Collection<Map<String, Object>> dailyReport(String currentHotleCode, LocalDate bDate) throws IOException, TemplateException;

    Collection<Map<String, Object>> paySummary(String currentHotleCode, LocalDate bDate) throws IOException, TemplateException;
}
