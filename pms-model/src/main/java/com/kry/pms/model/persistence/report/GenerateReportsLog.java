package com.kry.pms.model.persistence.report;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_reports_generate_log")
public class GenerateReportsLog {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(64)")
    private String id;
    @Column(columnDefinition = "varchar(100) COMMENT '酒店代码'")
    private String hotelCode;
    @Column(columnDefinition = "datetime COMMENT '夜审后生产报表时间'")
    private LocalDateTime auditDate;
    @Column(columnDefinition = "date COMMENT '营业日期'")
    private LocalDate businessDate;
    @Column(columnDefinition = "varchar(64) COMMENT '夜审人id'")
    private String userId;
    @Column(columnDefinition = "varchar(100) COMMENT '夜审用户名'")
    private String username;
    @Column(columnDefinition = "varchar(100) COMMENT '生成类型：AUTO自动  MANUAL手动'")
    private String type;
    @Column(columnDefinition = "varchar(100) COMMENT '导入报表状态：success:成功，error：失败'")
    private String auditStatus;
    @Column(columnDefinition = "varchar(2000) COMMENT '失败原因'")
    private String reason;
}
