package com.kry.pms.model.persistence.pay;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_wechat_refund_record")
public class WechatRefundRecord extends PersistenceModel {

    @Column(columnDefinition = "varchar(64) COMMENT '微信订单号'")
    private String transactionId;
    @Column(columnDefinition = "varchar(64) COMMENT '商户订单号'")
    private String outTradeNo;
    @Column(columnDefinition = "varchar(64) COMMENT '商户退款单号'")
    private String outRefundNo;
    @Column(columnDefinition = "varchar(64) COMMENT '微信退款单号'")
    private String refundId;
    @Column(columnDefinition = "varchar(64) COMMENT '退款金额，单位为分'")
    private String refundFee;
    @Column(columnDefinition = "varchar(64) COMMENT '订单总金额，单位为分'")
    private String totalFee;
    @Column(columnDefinition = "varchar(64) COMMENT '业务结果SUCCESS/FAIL'")
    private String resultCode;
    @Column(columnDefinition = "varchar(64) COMMENT '返回状态码'")
    private String returnCode;
    @Column(columnDefinition = "varchar(64) COMMENT '返回信息'")
    private String returnMsg;
    @Column(columnDefinition = "varchar(64) COMMENT '错误代码'")
    private String errCode;
    @Column(columnDefinition = "varchar(128) COMMENT '错误代码描述'")
    private String errCodeDes;

}
