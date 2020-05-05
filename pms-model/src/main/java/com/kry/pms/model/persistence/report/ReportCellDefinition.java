package com.kry.pms.model.persistence.report;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Data
@Table(name = "t_report_cell_def")
@Entity
public class ReportCellDefinition extends PersistenceModel {

    @Column(name = "key_")
    private String key;
    @Column(name = "type_")
    private String type;
    @Column(name = "value_")
    private String value;
    @Column
    private Integer sortNum;

}
