package com.kry.pms.model.persistence.report;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
public class ReportBase  {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(64)")
    private String id;
    @Column(columnDefinition = "varchar(100) COMMENT '酒店代码'")
    private String hotelCode;
    @Column(columnDefinition = "datetime COMMENT '夜审时间'")
    private LocalDateTime auditDate;
    @Column(columnDefinition = "varchar(64) COMMENT '夜审人id'")
    private String userId;
    @Column(columnDefinition = "varchar(100) COMMENT '夜审用户名'")
    private String username;
    @Column(columnDefinition = "date COMMENT '营业日期'")
    private LocalDate businessDate;
}
