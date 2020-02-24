package com.kry.pms.model.persistence.marketing;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.dict.Area;
import com.kry.pms.model.persistence.org.Employee;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_sales_men")
public class SalesMen extends PersistenceModel{
	@Column
	private String code;// 编码
	@Column
	private String selfCode;// 自编号
	@Column
	private String sex;// 性别
	@Column
	private String name;// 姓名
	@Column
	private LocalDate birthDate;// 生日
	@Column
	private String idCardNum;// 身份证号
	@Column
	private String certificateType;// 证件类型
	@Column
	private String contactMobile;// 电话
	@Column
	private String email;// 邮件
	@Column
	private String type;// 类型     F 全职       H  兼职
	@ManyToMany
	private List<Area> saleAreas;//销售区域
	@Column
	private LocalDate effectiveDate;//生效时间
	@Column
	private LocalDate expireDate;//失效时间
	@Column
	private String address;//地址
	@OneToOne
	private Employee employee;//员工
	@Column
	private String description;//备注
}
