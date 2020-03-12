package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 功能描述: <br>客户常用地址
 * 〈〉
 * @Author: huanghaibin
 * @Date: 2020/3/11 14:18
 */
@Data
@Entity
@Table(name = "t_cust_address")
public class CustAddress extends PersistenceModel {

    @Column
    private String name;//收件人
    @Column
    private String idType;//证件类型
    @Column
    private String idNum;//证件号码
    @Column
    private String mobile;//电话
    @Column
    private String region;//所在区域
    @Column
    private String addressInfo;//详细地址
    @Column
    private String zipCode;//邮编
    @Column
    private String isDefult;//是否默认Y:默认，N取消默认
    @Column
    private String customerId;
}
