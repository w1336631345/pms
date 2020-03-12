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
@Table(name = "t_cust_vehicle")
public class CustVehicle extends PersistenceModel {

    @Column
    private String snum;//序号
    @Column
    private String licensePlate;//车牌号
    @Column
    private String colors;//颜色
    @Column
    private String vehicleType;//车辆类型
    @Column
    private String customerId;//用户id
}
