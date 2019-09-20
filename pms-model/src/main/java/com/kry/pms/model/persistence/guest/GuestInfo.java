package com.kry.pms.model.persistence.guest;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.dict.CertificateType;
import com.kry.pms.model.persistence.dict.CountryOrRegion;
import com.kry.pms.model.persistence.sys.Account;

import lombok.Getter;
import lombok.Setter;

/**
 * 中心客户信息，各个酒店的customer为guestInfo的一个包装
 * 
 * @author Louis
 *
 */
@Entity
@Getter
@Setter
@Table(name = "t_guest_info")
public class GuestInfo extends PersistenceModel {
	@Column
	private String name;
	@Column
	private String mobile;
	@OneToOne
	private CertificateType certificateType;
	@Column
	private String idCardNum;
	@Column
	private String idcardAddress;
	@Column
	private String dateOfBirth;
	@Column
	private String licensingAuthority;
	@OneToOne
	private CountryOrRegion countryOrRegion;
}
