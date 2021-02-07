package com.kry.pms.model.persistence.log;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Entity
@Table(name = "t_log_update")
public class UpdateLog extends PersistenceModel {

    @Column
    private String product;
    @Column
    private String productType;//GO主单日志，RS房态日志
    @Column
    private String productName;
    @Column
    private String productValue;
    @Column
    private String oldValue;
    @Column
    private String newValue;
    @Column
    private String identifier;//标识符（用于查询）
    @Transient
    private String createName;

}
