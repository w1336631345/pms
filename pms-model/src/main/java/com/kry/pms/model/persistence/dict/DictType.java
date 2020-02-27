package com.kry.pms.model.persistence.dict;

import com.kry.pms.model.persistence.PersistenceModel;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
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
    private int safeLevel;
    @Column(name = "type_")
    private String type;
}

