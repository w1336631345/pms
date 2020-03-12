package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.marketing.DistributionChannel;
import com.kry.pms.model.persistence.marketing.MarketingSources;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 功能描述: <br>客户市场协议
 * @Author: huanghaibin
 * @Date: 2020/3/12 10:53
 */
@Data
@Entity
@Table(name = "t_cust_market")
public class CustMarket extends PersistenceModel {

    @ManyToOne
    private MarketingSources marketingSources;
    @ManyToOne
    private DistributionChannel distributionChannel;


}
