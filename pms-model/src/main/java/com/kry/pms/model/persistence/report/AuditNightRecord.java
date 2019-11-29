package com.kry.pms.model.persistence.report;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
public class AuditNightRecord {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(64)")
    private String id;
    @Column(columnDefinition = "varchar(100) COMMENT '酒店代码'")
    private String hotelCode;
    @Column(columnDefinition = "date COMMENT '夜审时间'")
    private LocalDate auditDate;
    @Column(columnDefinition = "varchar(64) COMMENT '夜审人id'")
    private String userId;
    @Column(columnDefinition = "varchar(100) COMMENT '夜审用户名'")
    private String username;
    @Column(columnDefinition = "varchar(100) COMMENT '夜审状态：start:开始，success:成功，error：失败'")
    private String auditStatus;
}
