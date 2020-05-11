
package com.kry.pms.model.persistence.goods;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "t_product")
public class Product extends PersistenceModel {
    @Column
    private String name;
    @Column(name = "price_type", columnDefinition = "varchar(40) COMMENT '定价类型 FREE_PRICE 不定价| FIXED_PRICE 固定定价'")
    private String priceType;
    @Column(name = "type_", columnDefinition = "varchar(40) COMMENT '产品类型GOODS 商品 |SERVICE 服务）'")
    private String type;//类型
    @Column(name = "code_")
    private String code;//费用代码
    @Column
    private String description1;
    @Column
    private String description2;
    @Column
    private String description3;
    @Column
    private Integer direction;//方向  1为收入，0为消费
    @Column(name = "price")
    private Double price;//价格
    @Column
    private Double taxRate;//税率
    @Column
    private String description;//描述
    @Column
    private String feeType;//费用类别（业绩归类 徐）
    @Column
    private String deptCode;//部门编码(余额归类 徐)
    @Column
    private String sysModel;//系统模块(使用范围 徐)
    @Column
    private String billCode;//账单编码
    @Column
    private String achievementType;//业绩归类
    @Column
    private String invoiceType;//发票类别
    @Column
    private Boolean isRebate;//是否Rabate
    @Column
    private Boolean needReason;//需要原因
    @Column
    private Integer shorNum;//排序
    @Column
    private String payType;//付款类别
    @Column
    private String innerCode;//内部码
    @Column
    private String simpleCode;//助记码
    @Column
    private Boolean isGroup;//集团吗
    @Column
    private String baseTableCol;//底表行
    @Column
    private String baseTableRow;//底表列

}
