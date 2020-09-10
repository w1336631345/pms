package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_member_level")
public class MemberLevel extends PersistenceModel {

    @Column
    private String code;//代码
    @Column
    private String remark;//描述
    @Column
    private String remark1;//描述1
    @ManyToOne
    private MemberIntegralType memberIntegralType;//积分模式
    @Column
    private String macCard;//物理卡
    @Column
    private String groupCode;//集团码
    @Column
    private String isUsed;//是否停用
    @ManyToOne
    private RoomPriceScheme roomPriceScheme;//房价码
    @Column
    private String restaurantCode;//餐饮码
    @Column
    private String passwrod;//初始密码
    @Column
    private String deductionType;//扣费模式(停用)
    @ManyToOne
    private MemberDeductionType memberDeductionType;//扣费模式
}
