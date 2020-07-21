package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.org.Hotel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "t_member_integral")
public class MemberIntegral extends PersistenceModel {

    @Column
    private String cardNum;//会员卡号
    @Column
    private String macNum;//物理卡号
    @Column
    private Double inIntegral;//增加积分
    @Column
    private Double outIntegral;//扣减积分
    @Column
    private Double roomPrice;//房费
    @Column
    private Double bos;//bos
    @Column
    private Double restaurant;//餐费
    @Column
    private Double market;//商场
    @Column
    private Double meeting;//会议
    @Column
    private Double entertainment;//娱乐
    @Column
    private Double others;//其它
    @ManyToOne
    private MemberIntegralType integralType;//积分模式
    @Column
    private String inOrOut;//加或者减
    @ManyToOne
    private Hotel hotel;//酒店
    @Column
    private String remark;//备注
    @Column
    private LocalDate consDate;//消费日期
    @Column
    private String orderNo;//单号
    @Column
    private Integer isOverdue;//是否过期(0有效，1过期)

}
