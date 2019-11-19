package com.kry.pms.model.persistence.report;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "t_report_business")
public class BusinessReport {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private Integer id;
    @Column(columnDefinition = "varchar(20) COMMENT '编号'")
    private String number_;
    @Column(columnDefinition = "varchar(100) COMMENT '项目'")
    private String project;
    @Column(columnDefinition = "varchar(50) COMMENT '项目代码'")
    private String projectCode;
    @Column(columnDefinition = "varchar(50) COMMENT '项目类型'")
    private String projectType;

    @Column(columnDefinition = "double COMMENT '本日发生'")
    private double totalDay;
    @Column(columnDefinition = "double COMMENT '日rebate'")
    private double rebateDay;
    @Column(columnDefinition = "double COMMENT '本月发生'")
    private double totalMonth;
    @Column(columnDefinition = "double COMMENT '本日发生'")
    private double rebateMonth;
    @Column(columnDefinition = "double COMMENT '月度rebate'")
    private double totalYear;
    @Column(columnDefinition = "double COMMENT '年度rebate'")
    private double rebateYear;

    @Column(columnDefinition = "varchar(100) COMMENT '酒店代码'")
    private String hotelCode;
    @Column(columnDefinition = "date COMMENT '记录的营业日期'")
    private LocalDate businessDate;
    @Column(columnDefinition = "varchar(50) COMMENT '排序'")
    private String sort;
}
