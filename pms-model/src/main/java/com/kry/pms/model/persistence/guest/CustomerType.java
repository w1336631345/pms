package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_customer_type")
public class CustomerType extends PersistenceModel {

    @Column
    private String code_;
    @Column
    private String name;

}
