package com.kry.pms.service.report;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.persistence.report.ReportTableDefinition;
import com.kry.pms.service.BaseService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportTableDefinitionService extends BaseService<ReportTableDefinition> {
    public DtoResponse<String> buildTemplate(String id);

    public DtoResponse<List<Map<String, Object>>> fetchData(String id, LocalDate quantityDate, String hotelCode);

}