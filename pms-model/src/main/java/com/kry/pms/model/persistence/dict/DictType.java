package com.kry.pms.model.persistence.dict;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_dict_type")
public class DictType {
    private String code;
    private String name;
    private String description;
    @OneToOne
    private DictType dictType;
}

