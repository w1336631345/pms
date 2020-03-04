package com.kry.pms.model.persistence.report;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_report_base_value")
public class ReportBaseValue extends PersistenceModel {
    @Column
    private LocalDate quantityDate;
    @Column
    private Integer quantityMothod;
    @Column
    private String name;
    @Column
    private String code;
    @Column(name="key_")
    private String key;
    @Column(name="value_")
    private Double value;
}
