package com.kry.pms.model.persistence.audit;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "t_audit_night_step")
public class AuditNightStep extends PersistenceModel {

    @Column
    private String stepName;//步骤名称
    @Column
    private LocalDateTime startTime;//开始时间
    @Column
    private LocalDateTime endTime;//结束时间
    @Column
    private String duration;//耗时
    @Column
    private LocalDate businessDate;//营业日期
    @Column
    private String processName;//存储过程名称
    @OneToMany(fetch = FetchType.LAZY , cascade=CascadeType.ALL)
    private List<AuditNightStepParam> params;//参数

}
