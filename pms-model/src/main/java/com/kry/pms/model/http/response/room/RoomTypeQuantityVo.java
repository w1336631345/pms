package com.kry.pms.model.http.response.room;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class RoomTypeQuantityVo implements Serializable {
	private String roomTypeId;
	private String roomTypeName;//房型
	private String roomTypeCode;
	private LocalDate quantityDate;
	private Integer roomCount;//房间总数
	private Integer availableTotal;// 可用数/可卖
	private Integer predictableTotal;// 可预订的
	private Integer bookingTotal = 0;//预订数
	private Integer willLeaveTotal = 0;//将离
	private Integer willArriveTotal = 0;//将到
	private Integer repairTotal = 0;//维修房
	private Integer lockedTotal = 0;//锁房
	private Integer usedTotal = 0;//在住
	private Integer reserveTotal = 0;//预留数
	private Integer hseTotal = 0;//自用房
	private Integer freeTotal = 0;//免费房
	private Double salesRate = 0.0;//售卖率

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
