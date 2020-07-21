package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 功能描述: <br>积分模式（类型）
 * 〈〉
 * @Author: huanghaibin
 * @Date: 2020/7/17 11:27
 */
@Data
@Entity
@Table(name = "t_member_integral_type")
public class MemberIntegralType extends PersistenceModel {

    @Column
    private String code;//代码
    @Column
    private String remark;//描述
    @Column
    private String englishRemark;//英文描述
    @Column
    private LocalDate startTime;//开始时间
    @Column
    private LocalDate endTime;//结束时间
    @Column
    private String isUsed;//是否启用
    @OneToMany(cascade={CascadeType.REMOVE})
    private List<MemberIntegralTypeInfo> memberIntegralTypeInfos;//积分明细
    @ManyToMany
    private List<MemberIntegralTypeDate> memberIntegralTypeDates;//特殊日期

}
