package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 功能描述: <br>客人特征
 * @Author: huanghaibin
 * @Date: 2020/3/12 16:37
 */
@Data
@Entity
@Table(name = "t_cust_features")
public class CustFeatures extends PersistenceModel {

    @Column
    private String special;//特殊要求
    @Column
    private String preference;//房间偏好
    @Column
    private String roomRequirment;//排房要求
    @Column
    private String hobby;//兴趣爱好
    @Column
    private String roomLayout;//客房布置
    @Column
    private String frontHobby;//前台喜好
    @Column
    private String restaurantHobby;//餐饮喜好
    @Column
    private String otherHobby;//其它喜好
    @Column
    private String autograph;//签名
    @Column
    private String photo;//照片
    @Column
    private String customerId;


}
