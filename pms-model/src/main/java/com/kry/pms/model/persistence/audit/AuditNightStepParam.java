package com.kry.pms.model.persistence.audit;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_audti_night_step_param")
public class AuditNightStepParam {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(64)")
    protected String id;
    private String mapKey;
    private String mapValue;
    private String valueType;
}
