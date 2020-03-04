package com.kry.pms.model.persistence.report;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "t_report_base_value_definition")
public class ReportBaseValueDefinition extends PersistenceModel {
    @Column
    private String name;
    @Column
    private String code;
    @Column(name = "type_")
    private String type;
    @Column
    private String key;
    @Column
    private String dataType;
    @Column
    private String dataValue;
}
