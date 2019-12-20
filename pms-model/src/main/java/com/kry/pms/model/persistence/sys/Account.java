package com.kry.pms.model.persistence.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.busi.Group;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.marketing.ProtocolCorpation;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.util.BigDecimalUtil;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_account")
public class Account extends PersistenceModel {
	@Column
	private String name;
	@Column
	private String code;
	@Transient
	private String roomNum;
	@Column
	private Double total;
	@Column(name="type_")
	private String type;
	@Column
	private Double cost;
	@Column
	private String contactName;
	@Column
	private String contactMobile;
	@Column
	private Double pay;
	@Column
	private Double creditLimit ;
	@Column
	private Double availableCreditLimit;
	@OneToOne
	private Employee operationEmployee;
	@Column(columnDefinition = "varchar(255) COMMENT '操作员备注'")
	private String operationRemark;
	@OneToOne
	private Customer customer;
	@OneToOne
	private Group group;
	@OneToOne
	private ProtocolCorpation protocolCorpation;
	@Column
	private Integer currentBillSeq;
	
	public Account() {
		super();
	}
	public Account(double cost,double pay) {
		super();
		this.cost = cost;
		this.pay = pay;
		this.total = BigDecimalUtil.sub(this.cost, this.pay);
		this.currentBillSeq = 0;
	}
}
