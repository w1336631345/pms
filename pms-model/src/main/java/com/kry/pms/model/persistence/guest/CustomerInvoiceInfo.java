package com.kry.pms.model.persistence.guest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

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
@Table(name = "t_customer_invoice_info")
public class CustomerInvoiceInfo extends PersistenceModel {
	@Column
	private String invoiceTitle;
	@Column
	private String companyNum;
	@Column
	private String invoiceType;
	@Column
	private String companyBankNum;
}
