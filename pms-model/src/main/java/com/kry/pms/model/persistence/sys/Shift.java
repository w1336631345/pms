package com.kry.pms.model.persistence.sys;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.org.Employee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_shift")
public class Shift extends PersistenceModel {
	@Column
	private String name;
	@Column
	private String code;
	@Column
	private String shiftCode;
	@Column
	private LocalDate businessDate;
	@OneToOne
	private Employee employee;
	@Column
	private LocalDateTime startTime;
}
