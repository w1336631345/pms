package com.kry.pms.model.http.response.report;

import com.kry.pms.base.PageResponse;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Data
public class CommomReportTableData {
    private String name;
    private String type;
    private String html;
    private List<ReportTableColumn> columns;
    private List<Map<String, Object>> data;
    private PageResponse<Map<String, Object>> page;
}
