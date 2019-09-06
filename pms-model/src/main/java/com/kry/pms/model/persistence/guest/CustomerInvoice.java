package com.kry.pms.model.persistence.guest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.Deal;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户发票
 * 
 * @author Louis
 *
 */
@Entity
@Getter
@Setter
@Table(name = "t_customer_invoice")
public class CustomerInvoice extends PersistenceModel {
	@OneToOne
	private CustomerInvoiceInfo customerInvoiceInfo;
	@Column
	private Double total;
	@OneToOne
	private Customer customer;
	@OneToOne
	private Bill bill;
	@OneToOne
	private Deal deal;
}
