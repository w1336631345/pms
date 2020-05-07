package com.kry.pms.model.persistence.report;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Table(name = "t_report_row_def")
@Entity
public class ReportRowDefinition extends PersistenceModel {
    @Column
    private String name;
    @Column
    private Integer sortNum;
    @ManyToMany(fetch = FetchType.EAGER)
    @OrderBy("sortNum")
    private Set<ReportCellDefinition> cells;
}
