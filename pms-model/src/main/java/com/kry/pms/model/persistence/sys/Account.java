package com.kry.pms.model.persistence.sys;

import java.time.LocalDate;

import javax.persistence.*;

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
	private Double total;//余额
	@Column(name="type_")
	private String type;
	@Column
	private String roomId;
	@Column
	private LocalDate startDate;//有效期起
	@Column
	private LocalDate endDate;//有效期止
	@Column
	private Double cost;//消费（总）
	@Column
	private String contactName;
	@Column
	private String contactMobile;
	@Column
	private Boolean holdArrow;//允许记账
	@Column
	private Double pay;//付款（总）
	@Column
	private Boolean isNoLimit;
	@Column
	private Double creditLimit ; //信用额度
	@Column
	private Double availableCreditLimit;//可用信用额度
	@OneToOne(fetch=FetchType.LAZY)
	private Employee operationEmployee;
	@Column(columnDefinition = "varchar(255) COMMENT '操作员备注'")
	private String operationRemark;
	@ManyToOne(fetch=FetchType.LAZY)
	private Customer customer;
	@OneToOne
	private Group group;
	@OneToOne
	private ProtocolCorpation protocolCorpation;
	@Column
	private Integer currentBillSeq;
	@Column
	private LocalDate extFeeDate;
	
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
