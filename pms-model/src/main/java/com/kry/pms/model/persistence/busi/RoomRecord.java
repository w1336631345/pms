package com.kry.pms.model.persistence.busi;

import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.guest.Customer;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_room_record")
public class RoomRecord extends PersistenceModel {
	@Column
	private LocalTime checkInTime;
	@Column
	private LocalTime checkOutTime;
	@Column
	private Integer guestCount;
	@OneToMany
	private List<Customer> guester;
}
