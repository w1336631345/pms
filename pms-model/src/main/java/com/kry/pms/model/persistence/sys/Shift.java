package com.kry.pms.model.persistence.sys;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.org.Employee;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "t_shift")
public class Shift extends PersistenceModel{
	private String name;
	private String code;
	private LocalDate businessDate;
	private Employee employee;
	private LocalDateTime startTime;
}
