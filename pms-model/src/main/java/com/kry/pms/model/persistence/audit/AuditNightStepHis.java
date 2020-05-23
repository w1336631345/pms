package com.kry.pms.model.persistence.audit;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_audit_night_step_his")
public class AuditNightStepHis extends PersistenceModel {

    @Column
    private LocalDateTime startTime;
    @Column
    private LocalDateTime endTime;
    @Column
    private String duration;//耗时
    @Column
    private LocalDate businessDate;


}
