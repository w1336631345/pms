package com.kry.pms.model.persistence.report;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
//前台入账报表
@Entity
@Data
@Table(name = "t_report_frontentry")
public class FrontEntryReport extends ReportBase {

    @Column(columnDefinition = "varchar(64) COMMENT '账号'")
    private String accountNum;
    @Column(columnDefinition = "varchar(64) COMMENT '房号'")
    private String roomNum;
    @Column(columnDefinition = "varchar(64) COMMENT '客户姓名'")
    private String custName;
    @Column(columnDefinition = "varchar(64) COMMENT '项目'")
    private String codeName;
    @Column(columnDefinition = "varchar(64) COMMENT '消费'")
    private String cost;
    @Column(columnDefinition = "varchar(64) COMMENT '创建时间'")
    private String createDate;
    @Column(columnDefinition = "varchar(64) COMMENT '收银员'")
    private String cashier;
    @Column(columnDefinition = "varchar(64) COMMENT '班次'")
    private String shiftCode;
    @Column(columnDefinition = "varchar(64) COMMENT '项目编码'")
    private String codeNum;
    @Column(columnDefinition = "varchar(64) COMMENT '项目大类型id'")
    private String categoryId;
    @Column(columnDefinition = "int(10) COMMENT '排序'")
    private Integer sort;
    @Column(columnDefinition = "varchar(64) COMMENT '是否跨行，1：跨列，0或空：不跨列'")
    private String isCrossColumn;
}
