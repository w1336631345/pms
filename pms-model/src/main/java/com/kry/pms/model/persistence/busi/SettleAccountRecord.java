package com.kry.pms.model.persistence.busi;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_settle_account_record")
public class SettleAccountRecord extends PersistenceModel {
	@Column
	private String shiftCode;
	@Column
	private String settleWay;
	@Column
	private String recordNum;
	@OneToOne
	private Account account;
	@OneToOne
	private Account targetAccount;
	@Column
	private String type;
	@OneToOne
	private Employee operationEmployee;
	@Column
	private LocalDateTime settleTime;
	@OneToOne
	private Employee cancleEmployee;
	@Column
	private LocalDateTime cancleTime;
	@Column
	private String remark;
	@Column
	private Double total;
	@Column
	private Integer billCount;
	@Column
	private String settleNum;
	@ManyToMany
	private List<Bill> bills;
	@ManyToMany
	private List<Bill> flatBills;
}
