package com.kry.pms.model.persistence.goods;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_product_type")
public class ProductType {

    @Id
    protected Integer id;

    @Column(columnDefinition = "varchar(64) default NUll COMMENT '产品ID'")
    private String productId;
    @Column(columnDefinition = "varchar(64) default NUll COMMENT '产品主类ID'")
    private String categoryId;
    @Column(columnDefinition = "varchar(100) default NUll COMMENT '产品类型代码'")
    private String code_;
    @Column(columnDefinition = "varchar(100) default NUll COMMENT '产品类型代码名称'")
    private String codeName;
    @Column(columnDefinition = "varchar(32) default NUll COMMENT '类型：MC：主营，RE：Rebate，SE：服务费，OT：其它'")
    private String type_;
    @Column(columnDefinition = "varchar(100) default NUll COMMENT '类型名称'")
    private String typeName;
    @Column(columnDefinition = "varchar(100) default NUll COMMENT '酒店编码'")
    private String hotelCode;

}
