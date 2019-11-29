package com.kry.pms.model.persistence.busi;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.org.Employee;

import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "t_daily_verify")
public class DailyVerify extends PersistenceModel{
	@Column
	private LocalDate verifyDate;
	@Column
	private String type;
	@OneToOne
	private Employee operationEmployee;
	@Column
	private LocalDate businessDate;
}
