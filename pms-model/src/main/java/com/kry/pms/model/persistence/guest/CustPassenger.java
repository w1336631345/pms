package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_cust_passenger")
public class CustPassenger extends PersistenceModel {
    @Column
    private String name;
    @Column
    private String englishFrist;
    @Column
    private String englishLast;
    @Column
    private String idCard;
    @Column
    private String mobile;
    @Column
    private String passengerType;
    @Column
    private String email;
}
