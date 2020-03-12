package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 功能描述: <br>发票信息（在用）
 * 〈〉
 * @Author: huanghaibin
 * @Date: 2020/3/11 16:05
 */
@Data
@Entity
@Table(name = "t_cust_invoice")
public class CustInvoice extends PersistenceModel {

    @Column
    private String compName;//企业名称
    @Column
    private String identCode;//纳税人识别号
    @Column
    private String registAddress;//注册地址
    @Column
    private String mobile;//电话
    @Column
    private String bank;//开户银行
    @Column
    private String bankAccount;//银行账号
    @Column
    private String invoiceType;//发票类别
    @Column
    private String customerId;
}
