package com.kry.pms.model.persistence.busi;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.model.persistence.sys.Shift;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_bill")
public class Bill extends PersistenceModel {
	@Column
	private String shiftCode;
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
	private Bill sourceBill;
	@Column
	private String currentSettleAccountRecordNum;
	@Column
	private String roomRecordId;
	
}
