package com.kry.pms.model.http.response.room;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class RoomTypeQuantityVo implements Serializable {
	private String roomTypeId;
	private String roomTypeName;
	private String roomTypeCode;
	private LocalDate quantityDate;
	private Integer roomCount;
	private Integer availableTotal;// 可用的
	private Integer predictableTotal;// 可预订的
	private Integer bookingTotal = 0;
	private Integer willLeaveTotal = 0;
	private Integer willArriveTotal = 0;
	private Integer repairTotal = 0;
	private Integer lockedTotal = 0;
	private Integer usedTotal = 0;
	private Integer reserveTotal = 0;
	private Integer hseTotal = 0;
	private Integer freeTotal = 0;

	public RoomTypeQuantityVo() {
	}

//	public RoomTypeQuantityVo(String roomTypeId, String roomTypeName, String roomTypeCode, LocalDate quantityDate,
//			Integer roomCount, Integer availableTotal, Integer predictableTotal, Integer bookingTotal,
//			Integer willLeaveTotal, Integer repairTotal, Integer lockedTotal, Integer usedTotal, Integer reserveTotal) {
//		super();
//		this.roomTypeId = roomTypeId;
//		this.roomTypeName = roomTypeName;
//		this.roomTypeCode = roomTypeCode;
//		this.quantityDate = quantityDate;
//		this.roomCount = roomCount;
//		this.availableTotal = availableTotal;
//		this.predictableTotal = predictableTotal;
//		this.bookingTotal = bookingTotal;
//		this.willLeaveTotal = willLeaveTotal;
//		this.repairTotal = repairTotal;
//		this.lockedTotal = lockedTotal;
//		this.usedTotal = usedTotal;
//		this.reserveTotal = reserveTotal;
//	}

}
