package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_vip_type")
public class VipType extends PersistenceModel {

    @Column(columnDefinition = "varchar(100) COMMENT '代码'")
    private String code_;
    @Column(columnDefinition = "varchar(200) COMMENT '名称'")
    private String name;
    @Column(columnDefinition = "varchar(1000) COMMENT '描述'")
    private String remark;
    @Column(columnDefinition = "varchar(100) COMMENT '是否启用'")
    private String isUsed;
    @Column(columnDefinition = "varchar(100) COMMENT '序号'")
    private String nums;
    @Column(columnDefinition = "varchar(100) COMMENT '类型'")
    private String vtype;
    @Column(columnDefinition = "varchar(100) COMMENT '集团编码'")
    private String clusterCode;

}
