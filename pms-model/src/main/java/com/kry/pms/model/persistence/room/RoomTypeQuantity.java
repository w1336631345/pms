package com.kry.pms.model.persistence.room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_room_type_quantity")
public class RoomTypeQuantity extends PersistenceModel {
	@OneToOne(fetch=FetchType.LAZY)
	private RoomType roomType;
	@Column
	private String roomTypeName;
	@Column
	private String roomTypeCode;
	@Column
	private LocalDate quantityDate;
	@Column
	private Integer roomCount;
	@Column
	private Integer availableTotal;// 可用的
	@Column
	private Integer predictableTotal;// 可预订的
	@Column
	private Integer bookingTotal = 0;
	@Column
	private Integer willLeaveTotal= 0;
	@Column
	private Integer repairTotal = 0;
	@Column
	private Integer lockedTotal =0;
	@Column
	private Integer usedTotal =0;

}
