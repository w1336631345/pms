package com.kry.pms.model.persistence.busi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.org.Employee;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_deal")
public class Deal extends PersistenceModel {
	@OneToOne
	private Customer customer;
	@Column
	private Double amountOfMoney;
	@OneToOne
	private Employee operationEmployee;
}
