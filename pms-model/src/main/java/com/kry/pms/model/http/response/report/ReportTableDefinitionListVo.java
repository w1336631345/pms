package com.kry.pms.model.http.response.report;

import lombok.Data;

@Data
public class ReportTableDefinitionListVo {
    private String id;
    private String name;
    private String groupKey;
    private String type;
    private String queryParams;

    public ReportTableDefinitionListVo() {
    }

    public ReportTableDefinitionListVo(String id, String name, String groupKey, String type,String queryParams) {
        this.id = id;
        this.name = name;
        this.groupKey = groupKey;
        this.type = type;
        this.queryParams = queryParams;
    }
}
