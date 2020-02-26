package com.kry.pms.model.persistence.marketing;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_discount_type")
public class DiscountType extends PersistenceModel {

    @Column
    private String code;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String isUsed;
    @Column
    private String sortNum;
    @Column
    private String type;
    @Column
    private String groupCode;

}
