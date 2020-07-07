package com.kry.pms.model.persistence.pay;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 功能描述: <br> 微信商户信息
 * @Author: huanghaibin
 * @Date: 2020/4/13 17:35
 */
@Data
@Entity
@Table(name="t_wechat_merchants")
public class WechatMerchants extends PersistenceModel {

    @Column(columnDefinition = "varchar(64) COMMENT '公众账号ID(服务商)'")
    private String appid;
    @Column(columnDefinition = "varchar(64) COMMENT '子商户公众账号ID'")
    private String subAppid;
    @Column(columnDefinition = "varchar(64) COMMENT '商户号(服务商)'")
    private String mchId;
    @Column(columnDefinition = "varchar(64) COMMENT '子商户号'")
    private String subMchId;
    @Column(columnDefinition = "varchar(64) COMMENT '微信支付的商户密钥'")
    private String secretKey;
    @Column(columnDefinition = "varchar(256) COMMENT '支付成功后的服务器回调url'")
    private String notifyUrl;
    @Column(columnDefinition = "varchar(64) COMMENT '签名方式'")
    private String signType;
    @Column(columnDefinition = "varchar(64) COMMENT '支付押金签名方式'")
    private String depositSignType;
    @Column(columnDefinition = "varchar(64) COMMENT '交易类型1'")
    private String tradeType1;
    @Column(columnDefinition = "varchar(64) COMMENT '交易类型2'")
    private String tradeType2;
    @Column(columnDefinition = "varchar(1000) COMMENT '证书地址'")
    private String cerPath;
    @Column(columnDefinition = "varchar(1000) COMMENT '酒店名称'")
    private String hotelName;
}
