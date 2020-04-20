package com.kry.pms.model.persistence.room;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_room_usage")
public class RoomUsage implements Serializable {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(columnDefinition = "varchar(64)")
	protected String id;
	@OneToOne(fetch = FetchType.EAGER)
	private RoomUsage preRoomUsage;
	@OneToOne(fetch = FetchType.EAGER)
	private RoomUsage postRoomUsage;
	@Column
	private Integer humanCount;
	@OneToOne(fetch = FetchType.LAZY)
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
	private String hotelCode;
	@Column
	private String businessInfo;
	@Column
	private Long duration;
	@ElementCollection
	@Cascade(org.hibernate.annotations.CascadeType.REMOVE)
	private Set<String> uniqueIds;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
