package com.kry.pms.model.persistence.pay;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_wechat_pay_record")
public class WechatPayRecord extends PersistenceModel {

    @Column(columnDefinition = "varchar(64) COMMENT '用户在商户appid下的唯一标识'")
    private String openid;//用户在商户appid下的唯一标识
    @Column(columnDefinition = "varchar(64) COMMENT '交易类型: MICROPAY 付款码支付'")
    private String tradeType;//交易类型: MICROPAY 付款码支付
    @Column(columnDefinition = "varchar(128) COMMENT '商品描述(如：房费、餐饮)'")
    private String body;//商品描述(如：房费、餐饮)
    @Column(columnDefinition = "varchar(64) COMMENT '订单总金额，单位为分'")
    private String totalFee; //订单总金额，单位为分
    @Column(columnDefinition = "varchar(64) COMMENT '微信订单号'")
    private String transactionId;//微信订单号
    @Column(columnDefinition = "varchar(64) COMMENT '商户订单号'")
    private String outTradeNo;//商户订单号
    @Column(columnDefinition = "varchar(64) COMMENT '订单生成时间'")
    private String timeEnd;//订单生成时间
    @Column(columnDefinition = "varchar(64) COMMENT '业务结果SUCCESS/FAIL'")
    private String resultCode;//业务结果SUCCESS/FAIL
    @Column(columnDefinition = "varchar(64) COMMENT '返回状态码'")
    private String returnCode;
    @Column(columnDefinition = "varchar(64) COMMENT '返回信息'")
    private String returnMsg;
    @Column(columnDefinition = "varchar(64) COMMENT '错误代码'")
    private String errCode;
    @Column(columnDefinition = "varchar(128) COMMENT '错误代码描述'")
    private String errCodeDes;

}
