package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModelTo;
import com.kry.pms.model.persistence.goods.Product;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name="t_member_recharge")
public class MemberRecharge extends PersistenceModelTo {
    @Column
    private String cardNum;//会员卡号
    @Column
    private String macNum;//物理卡号
    @ManyToOne
    private MemberInfo memberInfo;//会员卡
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
    private LocalDate limitationDate;//有效期
    @Transient
    private String operator;//操作员
    @Column
    private Double useAmount;//使用充值金额
    @Column
    private Double useGiveAmount;//使用赠送金额
    @Column
    private String rechargeOrUse;//充值（R）或使用（U）或过期（O）
    @Column
    private Integer isOverdue;//是否过期(0有效，1过期)
    @Column
    private Double overAmount;//过期金额
    @Column
    private Double overGiveAmount;//过期赠送金额
    @Transient
    private String shiftCode;//班次
    @Column
    private String settledNo;//结账单号
}
