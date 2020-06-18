package com.kry.pms.model.persistence.goods;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name="t_bos_goods_info")
public class BosGoodsInfo extends PersistenceModel {

    @Column
    private String code;
    @Column
    private String name;
    @Column
    private String englishName;
    @Column
    private String helpCode;
    @Column
    private String typeCode;
    @Column
    private Double costPrice;
    @Column
    private Double price1;
    @Column
    private Double price2;
    @Column
    private String specifications;
    @Column
    private String supplierCode;
    @Column
    private String supplierName;
    @ManyToOne
    private BosGoodsType bosGoodsType;

}
