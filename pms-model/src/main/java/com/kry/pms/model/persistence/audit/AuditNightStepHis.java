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
    private String stepName;//步骤名称
    @Column
    private LocalDateTime startTime;
    @Column
    private LocalDateTime endTime;
    @Column
    private String duration;//耗时
    @Column
    private LocalDate businessDate;
    @Column
    private String resultCode;//执行结果编码（stop:未执行，loading:执行中，success:成功，error:失败）
    @Column
    private String resultMsg;//执行结果信息


}
