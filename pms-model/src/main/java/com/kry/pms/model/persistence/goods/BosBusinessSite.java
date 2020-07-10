package com.kry.pms.model.persistence.goods;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "t_bos_business_site")
public class BosBusinessSite extends PersistenceModel {

    @Column
    private String code;//项目编码
    @Column
    private String name;//名称描述
    @ManyToOne
    private Product product;//项目
    @Column
    private Integer childSize;//小类代码长度
    @Column
    private String inventoryCode;//进销存
    @Column
    private String type;//类别
    @Column
    private String groupCode;//集团码
    @Column
    private String helpCode;//助记码
    @Column
    private String remark;
//    @OneToMany(cascade = CascadeType.PERSIST)
    @Transient
    private List<BosGoodsType> bosTypeList;

}
