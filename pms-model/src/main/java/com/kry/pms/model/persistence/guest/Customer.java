package com.kry.pms.model.persistence.guest;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.security.cert.Certificate;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.dict.CertificateType;
import com.kry.pms.model.persistence.dict.CountryOrRegion;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_customer")
public class Customer extends PersistenceModel {
	@Column
	private String name;
	@Column
	private String mobile;
	@OneToOne
	private CertificateType certificateType;
	@Column
	private String idcardNum;
	@Column
	private String idcardAddress;
	@Column
	private String dateOfBirth;
	@Column
	private String licensingAuthority;
	@OneToOne
	private CountryOrRegion countryOrRegion;
	@OneToMany
	private List<CustomerInvoiceInfo> invoiceInfos;
	@OneToOne
	private CustomerLevel customerLevel;
}
