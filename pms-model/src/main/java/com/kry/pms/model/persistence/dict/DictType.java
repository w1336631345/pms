package com.kry.pms.model.persistence.dict;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_dict_type")
public class DictType extends PersistenceModel {
    @Column
    private String code;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private Integer safeLevel;
    @Column(name = "type_")
    private String type;
}

