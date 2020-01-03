package com.kry.pms.model.persistence.room;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_room_usage")
public class RoomUsage implements Serializable{
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(columnDefinition = "varchar(64)")
	protected String id;
	@OneToOne(fetch = FetchType.LAZY)
	private RoomUsage preRoomUsage;
	@OneToOne(fetch = FetchType.LAZY)
	private RoomUsage postRoomUsage;
	@OneToOne
	private GuestRoom guestRoom;
	@Column
	private LocalDateTime startDateTime;
	@Column
	private LocalDateTime endDateTime;
	@Column
	private String usageStatus;
	@Column
	private String businesskey;
	@Column
	private String businessInfo;
	@Column
	private Long duration;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
