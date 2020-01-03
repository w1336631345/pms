package com.kry.pms.model.persistence.sys;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

//记账设置、团付设置
@Data
@Entity
@Table(name = "t_bookkeeping_set")
public class BookkeepingSet {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(64)")
    protected String id;
    @Column(columnDefinition = "varchar(64) COMMENT '项目编码'")
    private String productId;
    @Column(columnDefinition = "varchar(64) COMMENT '账号id'")
    private String accountId;
    @Column
    private int deleted;
    @Column
    private String hotelCode;

}
