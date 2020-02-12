package com.kry.pms.model.persistence.busi;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.org.Employee;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_bill_transfer_record")
public class BillTransferRecord {
	@Column
	private Double cost;
	@Column
	private Double pay;
	@Column
	private Double total;
	@Column
	private String type;
	@ManyToMany
	private List<Bill> sourceBills;
	@ManyToMany
	private List<Bill> targetBills;
	@OneToOne
	private Employee operationEmployee;
	@Column
	private LocalDateTime operationTime;
	@Column
	private String remark;
}
