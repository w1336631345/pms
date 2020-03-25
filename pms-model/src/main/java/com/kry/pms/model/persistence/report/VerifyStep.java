package com.kry.pms.model.persistence.report;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class VerifyStep extends PersistenceModel {
    @Column
    private String name;
    @Column
    private String quantityType;
    @Column
    private String verifyType;
    @Column
    private Integer seq;
    @Column
    private String type;
    @Column
    private String headerType;//头部类型
    @Column
    private String headerValue;//表头数据
    @Column
    private String dataType;//数据类型
    @Column
    private String dataValue;//数据值
    @Column
    private Boolean forceCheck;
}
