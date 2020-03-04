package com.kry.pms.dao.report;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.report.ReportBaseValueDefinition;

import java.util.List;

public interface ReportBaseValueDefinitionDao extends BaseDao<ReportBaseValueDefinition>{

    List<ReportBaseValueDefinition> findByHotelCodeAndExecuteType(String hotelCode, String executeTypeAfterNightAudt);
}
