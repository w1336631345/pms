package com.kry.pms.model.http.response.busi;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.marketing.ProtocolCorpation;

import lombok.Data;
@Data
public class CheckInRecordListVo {
	private String id;
	private String status;
	private String name;
	private String orderType;
	private String orderNum;
	private Integer humanCount;
	private Integer roomCount;
	private Integer chrildrenCount;
	private LocalDateTime arriveTime;
	private LocalDate startDate;
	private Integer checkInCount = 0;
	private Integer days;
	private String holdTime;
	private LocalDateTime leaveTime;
	private String contactMobile;
	private String type;
	private String contactName;
	private ProtocolCorpation protocolCorpation;
	private String remark;
	public CheckInRecordListVo(){
		
	}
	public CheckInRecordListVo(CheckInRecord cir) {
		this.id = cir.getId();
		this.status = cir.getStatus();
		this.name = cir.getName();
		this.orderNum = cir.getOrderNum();
		this.orderType = cir.getOrderType();
		this.humanCount = cir.getHumanCount();
		this.roomCount = cir.getRoomCount();
		this.chrildrenCount = cir.getChrildrenCount();
		this.arriveTime = cir.getArriveTime();
		this.leaveTime = cir.getLeaveTime();
		this.days = cir.getDays();
		this.holdTime = cir.getHoldTime();
		this.type = cir.getType();
		this.contactName = cir.getContactName();
		this.contactMobile = cir.getContactMobile();
		this.protocolCorpation = cir.getProtocolCorpation();
		this.remark = cir.getRemark();
	}
}
