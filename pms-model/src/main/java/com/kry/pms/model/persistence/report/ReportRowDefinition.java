package com.kry.pms.model.persistence.report;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Table(name = "t_report_row_def")
@Entity
public class ReportRowDefinition extends PersistenceModel {
    @Column
    private String name;
    @Column
    private Integer sortNum;
    @ManyToMany
    private List<ReportCellDefinition> cells;
}
