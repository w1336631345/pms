package com.kry.pms.model.persistence.goods;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

//@Entity
@Data
//@Table(name = "t_product_type")
public class ProductType {

//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid")
//    @Column(columnDefinition = "varchar(64)")
    protected String id;


    private String productId;
    private String categoryId;
    private String code_;
    private String type_;
    private String typeName;

}
