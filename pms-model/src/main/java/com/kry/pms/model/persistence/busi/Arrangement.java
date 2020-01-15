package com.kry.pms.model.persistence.busi;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Data
@Table(name="t_arrangement")
public class Arrangement extends PersistenceModel {

    @Column
    private String name;
    @Column
    private String code_;
    @Column
    private String englishName;
    @Column
    private String remark;
}
