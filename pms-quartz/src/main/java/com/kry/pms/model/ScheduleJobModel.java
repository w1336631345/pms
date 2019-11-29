package com.kry.pms.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;

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

    @Column(columnDefinition = "datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    protected LocalDateTime createDate;
    @CreatedBy
    @Column(columnDefinition = "varchar(255) COMMENT '创建人'")
    protected String createUser;
    @Column(columnDefinition = "datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'")
    protected LocalDateTime updateDate;
    @CreatedBy
    @Column(columnDefinition = "varchar(255) COMMENT '修改人'")
    protected String updateUser;

    @Column(columnDefinition = "varchar(64) COMMENT '酒店编码'")
    private String hotelCode;
    @Column(columnDefinition = "varchar(32) default 'AUDIT' COMMENT 'ALL全部执行  AUDIT夜审入账 NORMAL平常的'")
    private String type_;
}
