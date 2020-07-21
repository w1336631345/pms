package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.goods.Product;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="t_member_recharge")
public class MemberRecharge extends PersistenceModel {
    @Column
    private String cardNum;//会员卡号
    @Column
    private String macNum;//物理卡号
    @ManyToOne
    private Product payType;//付款方式
    @Column
    private Double amount;//充值金额
    @Column
    private String transationId;//支付订单号
    @Column
    private String outTradeNo;//外部订单号
    @Column
    private String giveType;//赠送方式
    @Column
    private Double giveAmount;//赠送金额
    @Column
    private LocalDate rechargeDate;//充值日期
    @Column
    private String bankNum;//银行卡号
    @Column
    private String remark;//备注
    @Column
    private LocalDate termOfValidity;//有效期
    @Transient
    private String operator;//操作员
}
