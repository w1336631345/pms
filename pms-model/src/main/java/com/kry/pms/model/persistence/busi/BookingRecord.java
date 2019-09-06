package com.kry.pms.model.persistence.busi;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.marketing.MarketingSources;
import com.kry.pms.model.persistence.marketing.ProtocolCorpation;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.room.RoomType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_booking_record")
public class BookingRecord extends PersistenceModel {
	@Column
	public Integer roomCount;
	@OneToOne
	public RoomType roomType;
	@Column
	private String contactName;
	@Column
	private String contactMobile;
	@OneToOne
	private Employee operationEmployee;
	@OneToOne
	private Employee marketEmployee;
	@Column
	private LocalDateTime arriveTime;
	@Column
	private LocalDateTime leaveTime;
	@Column
	private Integer days;
	@Column
	private Integer humanCount;
	@Column
	private LocalDateTime retainTime;
	@Column
	private String remark;
	@Column
	private Boolean priceSecret;
	@Column
	private Integer checkInRoomCount;
	@Column
	private Integer checkInHumanCount;
	@OneToOne
	private ProtocolCorpation protocolCorpation;
	@OneToOne
	private MarketingSources marketingSources;

}
