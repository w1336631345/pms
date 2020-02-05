package com.kry.pms.model.persistence.guest;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

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
	private CustomerLevel customerLevel;//客户等级
	@OneToOne
	private User user;
	@OneToOne
	private GuestInfo guestInfo;//房间信息
	@Column
	private Integer checkInCount;//入住人数
	@Column
	private String currentStatus;//
	@OneToOne
	private CheckInRecord currentRecord;//入住记录
	@Column
	private Double costTotal;
	@Column
	private String certificateType;//证件类型
	@Column
	private String idCardNum;//身份证号码
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
	@Column
	private LocalDate birthday;//生日
	@Column
	private String language;//语言
	@Column
	private LocalDate effectiveDate;//有效日期
	@Column
	private String numCode;//编号
	@ManyToOne
	private CustomerType customerType;//类型

	@Column
	private String education;//学历
	@Column
	private String graduateSchool;//毕业学校
	@Column
	private String vip;
	@Column
	private String compNum;//单位编号
	@Column
	private String comp;//单位
	@Column
	private String job;//工作职业
	@Column
	private String position;//职位
	@Column
	private String email;//邮箱
	@Column
	private String mobile;//电话
	@Column
	private String landline; //座机
	@Column
	private String fax;//传真
	@Column
	private String zipCode;//邮编
	@Column
	private String others;//其他


}
