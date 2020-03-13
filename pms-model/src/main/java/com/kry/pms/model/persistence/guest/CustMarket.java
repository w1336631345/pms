package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.marketing.DistributionChannel;
import com.kry.pms.model.persistence.marketing.MarketingSources;
import com.kry.pms.model.persistence.org.Department;
import com.kry.pms.model.persistence.sys.Account;
import lombok.Data;

import javax.persistence.*;

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
    @Column
    private String creditRating;//信用等级
    @Column
    private String industryType;//行业类别
    @Column
    private String enterpriseType; //企业类别
    @Column
    private String ticketCorp;//开票单位
    @Column
    private String dutyParagraph;//税号
    @Column
    private String addressMobile;//地址电话
    @Column
    private String bankAccount;//开户银行账号
    @OneToOne
    private Account account;//应收账号
    @Column
    private String memberAccount;//会员账号（应该是对象，不过会员暂时没做）
    @ManyToOne
    private Department department;//归属部门
    @Column
    private String custType;//客户类型（白名单/黑名单）
    @Column
    private String blackListType;//黑名单类型
    @Column
    private String blackReson;//拉黑理由
    @Column
    private String agreementType;//协议类别
    @Column
    private String customerId;

}
