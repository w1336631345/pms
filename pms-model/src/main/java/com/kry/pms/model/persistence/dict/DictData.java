package com.kry.pms.model.persistence.dict;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "t_dict_data")
public class DictData extends PersistenceModel {
    @Column
    private String code;//编码
    @Column
    private String name;//名称
    @Column
    private String description; //描述
    @Column
    private String dictTypeCode;//类型
    @Column
    private Integer sortNum;//排序
    @Column
    private Boolean isGroupCode;//集团码
    @Column
    private Integer safeLevel;
    @Column
    private String description1;
    @Column
    private String description2;
    @Column
    private String description3;
    @Column
    private String otherStatus;
}
