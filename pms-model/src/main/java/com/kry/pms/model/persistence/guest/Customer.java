package com.kry.pms.model.persistence.guest;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.security.cert.Certificate;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.dict.CertificateType;
import com.kry.pms.model.persistence.dict.CountryOrRegion;
import com.kry.pms.model.persistence.sys.Account;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_customer")
public class Customer extends PersistenceModel {
	@OneToMany
	private List<CustomerInvoiceInfo> invoiceInfos;
	@OneToOne
	private CustomerLevel customerLevel;
	@OneToOne
	private Account account;
	@OneToOne(fetch=FetchType.EAGER)
	private GuestInfo guestInfo;
}
