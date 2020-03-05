package com.kry.pms.model.http.response.report;

import lombok.Data;

import java.util.List;
@Data
public class ReportTableColumn {
    String title;
    String key;
    String align;
    int width;
    String fixed;
    List<ReportTableColumn> children;
}
