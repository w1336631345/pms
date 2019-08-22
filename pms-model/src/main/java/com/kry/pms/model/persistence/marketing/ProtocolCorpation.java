package com.kry.pms.model.persistence.marketing;

import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.goods.ProductCategory;
import com.kry.pms.model.persistence.guest.Customer;

import lombok.Getter;
import lombok.Setter;

/**
 * 协议用户
 * 
 * @author Louis
 *
 */
@Entity
@Getter
@Setter
@Table(name = "t_protocol_corpation")
public class ProtocolCorpation extends PersistenceModel {
	@Column
	private String name;
	@Column
	private String mobile;
	@Column
	private String tel;
	@Column
	private String homeUrl;
	@Column
	private String province;
	@Column
	private String city;
	@Column
	private String area;
	@Column(columnDefinition = "double NULL DEFAULT 0.0 COMMENT '授信额度'")
	private Double creditLine;
	@Column
	private String address;
	@Column
	private String description;
	@Column
	private String contactName;
	@Column
	private String contactMobile;
	@OneToMany
	private List<Customer> authorizeCustomers;
	@OneToOne
	private RoomPriceScheme roomPriceScheme;
	@OneToMany
	private List<ProductCategory> categorys;
}
