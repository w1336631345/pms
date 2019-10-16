package com.kry.pms.model.persistence.busi;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.sys.Account;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_checkin_record")
public class CheckInRecord extends PersistenceModel {
	@OneToOne
	private Customer customer;
	@OneToOne
	private GuestRoom guestRoom;
	@Column(name="name_")
	private String name;
	@Column
	private LocalDateTime arriveTime;
	@Column
	private LocalDate startDate;
	@Column
	private Integer days;
	@Column
	private LocalDateTime leaveTime;
	@Column(columnDefinition = "varchar(64) default '0000' COMMENT '入住编号'")
	private String checkInSn;
	@Column(name = "type_")
	private String type;
	@ManyToOne
	private Group group;
	@OneToOne
	private Account account;
}
