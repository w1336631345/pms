package com.kry.pms.model.persistence.dict;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name="t_dict_data")
public class DictData {
    private String code;
    private String name;
    private String description;

}
