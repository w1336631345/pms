package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 功能描述: <br>投诉建议
 * @Author: huanghaibin
 * @Date: 2020/3/12 17:00
 */
@Data
@Entity
@Table(name = "t_cust_proposal")
public class CustProposal extends PersistenceModel {
    @CreatedDate
    @Column(updatable = false, columnDefinition = "datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '投诉建议日期'")
    private LocalDate proposalTime;//投诉建议日期
    @Column
    private String keyword;//关键字
    @Column
    private String proposalType;//类型
    @Column
    private String content;//类容
    @Column
    private String statusType;//详细情况
    @Column
    private String handleby;//处理人
    @Column(updatable = false, columnDefinition = "datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '处理时间 '")
    private LocalDateTime handleTime;//处理时间
    @Column
    private String customerId;

}
