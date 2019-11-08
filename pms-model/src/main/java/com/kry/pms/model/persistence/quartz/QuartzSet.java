package com.kry.pms.model.persistence.quartz;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
@Entity
@Data
@Table(name = "t_quartz_set")
public class QuartzSet {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(64)")
    protected String id;
    @Column(columnDefinition = "varchar(50) COMMENT '定时表达式'")
    private String cron;
    @Column(columnDefinition = "varchar(50) COMMENT '定时时间'")
    private String cronTime;
    @Column(columnDefinition = "datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    private Date createDate;
    @Column(columnDefinition = "varchar(64) default '0000' COMMENT '酒店编码'")
    private String hotelCode;
    @Column(columnDefinition = "varchar(255) COMMENT '创建人'")
    private String createUser;
    @Column(columnDefinition = "datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'")
    private Date updateDate;
    @Column(columnDefinition = "varchar(255) COMMENT '修改人'")
    private String updateUser;
    @Column(columnDefinition = "varchar(32) default 'draft' COMMENT '状态'")
    private String status;
}
