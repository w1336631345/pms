package com.kry.pms.dao.report;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.http.response.report.ReportTableDefinitionListVo;
import com.kry.pms.model.persistence.report.ReportTableDefinition;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportTableDefinitionDao extends BaseDao<ReportTableDefinition> {
    @Query(value = "select new com.kry.pms.model.http.response.report.ReportTableDefinitionListVo(id,name,groupKey,type,queryParams) from ReportTableDefinition where groupKey=?1 and deleted = 0")
    List<ReportTableDefinitionListVo> groupKey(String groupKey);
}
