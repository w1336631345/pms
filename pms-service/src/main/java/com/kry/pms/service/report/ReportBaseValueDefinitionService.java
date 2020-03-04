package com.kry.pms.service.report;

import com.kry.pms.model.persistence.report.ReportBaseValueDefinition;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface ReportBaseValueDefinitionService extends BaseService<ReportBaseValueDefinition>{
    public List<ReportBaseValueDefinition> queryAfterNightAudt(String hotelCode);
}