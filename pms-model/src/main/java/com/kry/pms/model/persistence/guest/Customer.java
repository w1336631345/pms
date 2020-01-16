package com.kry.pms.model.persistence.guest;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.busi.CheckInRecord;
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
	private String mobile;
	@OneToOne
	private GuestInfo guestInfo;
	@Column
	private Integer checkInCount;
	@Column
	private String currentStatus;
	@OneToOne
	private CheckInRecord currentRecord;
	@Column
	private Double costTotal;
	@Column
	private String certificateType;
	@Column
	private String idCardNum;
	@Column
	private String credit;//信用
	@Column
	private String nationality;//国籍
	@Column
	private String nation;//民族
	@Column
	private String gender;//性别
	@Column
	private Integer age;//年龄
	@Column
	private String address;//地址
}
