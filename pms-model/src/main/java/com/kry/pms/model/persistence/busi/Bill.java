package com.kry.pms.model.persistence.busi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.marketing.ProtocolCorpation;
import com.kry.pms.model.persistence.org.Employee;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_bill")
public class Bill extends PersistenceModel {
	@Column
	private Double total;
	@Column
	private String type;
	@OneToOne
	private Employee operationEmployee;
	@Column(columnDefinition = "varchar(255) COMMENT '操作员备注'")
	private String operationRemark;
	@OneToOne
	private Customer customer;
	@OneToOne
	private Group group;
	@OneToOne
	private ProtocolCorpation ProtocolCorpation;
	@Column
	private Integer currentItemSeq;
	@Column
	private String paymentStatus;
	@Column
	private String settlementStatus;

}
