package com.kry.pms.model.persistence.report;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "t_report_table_definition")
public class ReportTableDefinition extends PersistenceModel {
    @Column
    private String code;
    @Column
    private String name;
    @Column(name = "type_")
    private String type;
    @Column
    private String groupKey;
    @Column
    private String headerType;
    @Column
    private String headerValue;
    @Column
    private String dataType;
    @Column
    private String dataValue;
    @Column
    private String webTemplateType;
    @Column
    private String webTemplate;
    @Column
    private String exportTemplete;
    @Column
    private String exportTempleteType;
    @Column
    private String params;
}
