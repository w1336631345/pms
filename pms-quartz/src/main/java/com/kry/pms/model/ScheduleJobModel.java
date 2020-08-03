package com.kry.pms.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "q_schedule_job")
@Entity
public class ScheduleJobModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    // 任务group名称
    @Column(name = "group_name")
    private String groupName;

    // 任务job名称
    @Column(name = "job_name")
    private String jobName;

    // cron表达式
    private String cron;

    @Column(columnDefinition = "varchar(100) COMMENT '定时时间'")
    private String startTime;

    // 0 - 代表正在执行  1 - 已删除  2 - 暂停
    @Column(columnDefinition = "tinyint(1) default '2' COMMENT '0 - 正在执行  1 - 已删除  2 - 暂停'")
    private Integer status;
    @CreatedDate
    @Column(columnDefinition = "datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    protected LocalDateTime createDate;
    @CreatedBy
    @Column(columnDefinition = "varchar(255) COMMENT '创建人'")
    protected String createUser;
    @LastModifiedDate
    @Column(columnDefinition = "datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'")
    protected LocalDateTime updateDate;
    @LastModifiedBy
    @Column(columnDefinition = "varchar(255) COMMENT '修改人'")
    protected String updateUser;

    @Column(columnDefinition = "varchar(64) COMMENT '酒店编码'")
    private String hotelCode;
    @Column(columnDefinition = "varchar(32) default 'AUDIT' COMMENT 'ALL全部执行  AUDIT夜审入账 NORMAL平常的'")
    private String type_;

    //自定义定时任务配置
    //1、class_path配置类名全路径，如：com.kry.pms.service.quratz.impl.QuartzSetServiceImpl
    //2、class_name配置spring管理的类名，spring默认bean首字母是小写，如：quartzSetServiceImpl
    //3、method_name直接配置定时要调用的方法名称（注：默认方法都会传入hotelCode参数），如：addTest
    //4、暂时不支持多参数
    @Column(columnDefinition = "varchar(1000) COMMENT '描述'")
    private String remark;
    @Column(columnDefinition = "varchar(500) COMMENT '方法所在类全路径'")
    private String classPath;
    @Column(columnDefinition = "varchar(64) COMMENT '对象名称'")
    private String className;
    @Column(columnDefinition = "varchar(256) COMMENT '方法名称'")
    private String methodName;
    @Column(columnDefinition = "varchar(64) COMMENT '参数类型'")
    private String paramsType;
    @Column(columnDefinition = "varchar(64) COMMENT '参数值'")
    private String paramsValue;
}
