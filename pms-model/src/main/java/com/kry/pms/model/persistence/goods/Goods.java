package com.kry.pms.model.persistence.goods;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.prefs.Preferences;

@Data
@Entity
@Table(name = "t_goods")
public class Goods extends PersistenceModel {

    @Column(columnDefinition = "double COMMENT '价格'")
    private Double price;
    @Column(columnDefinition = "varchar(64) COMMENT '类型'")
    private String type_;
    @Column(columnDefinition = "varchar(200) COMMENT '名称'")
    private String name;
    @Column(columnDefinition = "varchar(64) COMMENT '编码'")
    private String code_;
    @Column(columnDefinition = "varchar(1000) COMMENT '描述'")
    private String remark;

}
