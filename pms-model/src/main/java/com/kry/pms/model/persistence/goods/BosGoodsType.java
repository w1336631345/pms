package com.kry.pms.model.persistence.goods;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name="t_bos_goods_type")
public class BosGoodsType extends PersistenceModel {

    @Column
    private String code;
    @Column
    private String name;
    @Column
    private String remark;
    @Column
    private String memoryCode;
    @ManyToOne
    private BosBusinessSite bosBusinessSite;
}
