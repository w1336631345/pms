package com.kry.pms.service.report;

import com.kry.pms.model.persistence.sys.User;

import java.util.List;
import java.util.Map;

public interface AuditReportService {
    List<Map<String, Object>> auditNight(User user, String businessDate);

    List<Map<String, Object>> receivables(User user, String businessDate);
}
