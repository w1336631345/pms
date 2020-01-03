package com.kry.pms.model.persistence.report;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "t_report_front_receive")
public class FrontReceiveReport extends ReportBase {

    @Column(columnDefinition = "varchar(64) COMMENT '收款'")
    private String pay;

    @Column(columnDefinition = "varchar(64) COMMENT '收银员id'")
    private String empId;

    @Column(columnDefinition = "varchar(64) COMMENT '收银员'")
    private String empName;

    @Column(columnDefinition = "varchar(64) COMMENT '班次'")
    private String shiftCode;

    @Column(columnDefinition = "varchar(64) COMMENT '项目名称'")
    private String codeName;

    @Column(columnDefinition = "varchar(64) COMMENT '部门'")
    private String dept;

    @Column(columnDefinition = "varchar(64) COMMENT '部门id'")
    private String deptId;
}
