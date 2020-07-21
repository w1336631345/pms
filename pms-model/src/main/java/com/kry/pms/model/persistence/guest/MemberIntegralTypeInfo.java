package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.goods.Product;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 功能描述: <br>积分模式-积分规则明细
 * 〈〉
 * @Author: huanghaibin
 * @Date: 2020/7/17 11:28
 */
@Data
@Entity
@Table(name = "t_member_integral_type_info")
public class MemberIntegralTypeInfo extends PersistenceModel {

    @Column
    private String code;//业绩归类代码
    @Column
    private String achievement;//业绩归类
    @Column
    private Integer startStep;//起步（元）
    @Column
    private Integer setpLength;//步长（元）
    @Column
    private Double proportion;//积分比例


}
