package com.kry.pms.model.persistence.report;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

//收款报表
@Entity
@Data
@Table(name = "t_report_receivables")
public class ReceivablesReport extends ReportBase {

    @Column(columnDefinition = "varchar(64) COMMENT '部门id'")
    private String dempId;
    @Column(columnDefinition = "varchar(100) COMMENT '部门'")
    private String demp;
    @Column(columnDefinition = "varchar(50) COMMENT '班组'")
    private String groupType;
    @Column(columnDefinition = "varchar(100) COMMENT '收银员'")
    private String cashier;
    @Column(columnDefinition = "varchar(50) COMMENT '现金'")
    private String cash;
    @Column(columnDefinition = "varchar(50) COMMENT '支票'")
    private String checkPay;
    @Column(columnDefinition = "varchar(50) COMMENT '银行卡'")
    private String bankCard;
    @Column(columnDefinition = "varchar(50) COMMENT '微信支付'")
    private String wechatPay;
    @Column(columnDefinition = "varchar(50) COMMENT '支付宝支付'")
    private String alipay;
    @Column(columnDefinition = "varchar(50) COMMENT '其他'")
    private String otherPay;
    @Column(columnDefinition = "varchar(50) COMMENT '合计'")
    private String total;


}
