package com.kry.pms.model.persistence.guest;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.sys.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_customer")
public class Customer extends PersistenceModel {
	@Column
	private String name;
	@OneToMany
	private List<CustomerInvoiceInfo> invoiceInfos;
	@OneToOne
	private CustomerLevel customerLevel;
	@OneToOne
	private User user;
	@Column
	private String idCardNum;
	@OneToOne(fetch = FetchType.EAGER)
	private GuestInfo guestInfo;
}
