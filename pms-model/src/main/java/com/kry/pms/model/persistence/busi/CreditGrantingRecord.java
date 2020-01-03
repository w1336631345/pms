package com.kry.pms.model.persistence.busi;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "t_credit_granting_record")
public class CreditGrantingRecord extends PersistenceModel{
	@OneToOne
	private Account account;
	@Column(name = "value_")
	private Double value;
	@Column
	private String type;
	@OneToOne
	private Account grantingAccount;
	@OneToOne
	private Employee grantingEmployee;
	@OneToOne
	private Employee operationEmployee;
	@Column
	private LocalDate periodOfValidity;
	@Column
	private Double grantingLimit;
	@Column
	private String cardNum;
	@Column
	private String receiptNum;
	@Column
	private String grantCode;
	@Column
	private String remark;
	
}
