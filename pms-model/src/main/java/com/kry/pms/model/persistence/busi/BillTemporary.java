package com.kry.pms.model.persistence.busi;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
//入账临时表
@Entity
@Data
@Table(name = "t_bill_temporary")
public class BillTemporary extends PersistenceModel {
    @OneToOne
    private Account account;
    @Column
    private Double total;
    @Column
    private Integer quantity;
    @OneToOne
    private Product product;
    @Column
    private String type;
    @Column
    private Double cost;
    @Column
    private Double pay;
    @OneToOne
    private Employee operationEmployee;
    @Column(columnDefinition = "varchar(255) COMMENT '操作员备注'")
    private String operationRemark;
    @Column
    private Integer currentItemSeq;
    @Column
    private String paymentStatus;
    @Column
    private Integer billSeq;
    @Column
    private String settlementStatus;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_id")
    private List<BillItem> items;
    @Column
    private LocalDate businessDate;
    @Column
    private String receiptNum;
    @Column
    private String remark;
    @Column
    private String transferFlag;
    @OneToOne
    private BillTemporary sourceBill;
    @Column
    private String currentSettleAccountRecordNum;
    @Column
    private String roomRecordId;
}
