package com.kry.pms.model.persistence.report;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_audit_night_step")
public class AuditNightStep extends PersistenceModel {

    @Column
    private String stepName;
    @Column
    private LocalDateTime startTime;
    @Column
    private LocalDateTime endTime;
    @Column
    private String duration;
    @Column
    private LocalDate businessDate;

}
