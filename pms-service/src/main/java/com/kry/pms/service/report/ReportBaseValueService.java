package com.kry.pms.service.report;

import com.kry.pms.model.persistence.report.ReportBaseValue;
import com.kry.pms.service.BaseService;

public interface ReportBaseValueService extends BaseService<ReportBaseValue>{

    boolean executeAfterNightAudit(String hotelCode);
}