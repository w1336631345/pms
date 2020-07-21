package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
/**
 * 功能描述: <br>积分模式-特殊日期
 * 〈〉
 * @Author: huanghaibin
 * @Date: 2020/7/17 11:28
 */
@Data
@Entity
@Table(name = "t_member_integral_type_date")
public class MemberIntegralTypeDate extends PersistenceModel {

    @Column
    private String code;//代码
    @Column
    private String remark;//描述
    @Column
    private String remark1;//描述1
    @Column
    private LocalDate startTime;//开始日期
    @Column
    private LocalDate endTime;//结束日期
    @Column
    private String coefficient;//积分系数
    @Column
    private String groupCode;//集团码

}
