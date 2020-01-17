package com.kry.pms.model.http.response.busi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.kry.pms.model.http.response.marketing.CustomerVo;
import com.kry.pms.model.persistence.busi.CheckInRecord;

import lombok.Data;

@Data
public class CheckInRecordVo {
	private String id;
	private String name;
	private String orderNum;
	private String groupType;
	private LocalDateTime arriveTime;
	private LocalDateTime leaveTime;
	private String protocolCorpationName;
	private String protocolCorpationId;
	private Double purchasePrice;
	private Double personalPrice;
	private CustomerVo customer;

	public CheckInRecordVo() {

	}

	public static CheckInRecordVo convert(CheckInRecord cir) {
		if(cir==null) {
			return null;
		}
		CheckInRecordVo cirv = new CheckInRecordVo();
		BeanUtils.copyProperties(cir, cirv);
		if (cir.getCustomer() != null) {
			cirv.setCustomer(CustomerVo.convert(cir.getCustomer()));
		}
		if (cir.getProtocolCorpation() != null) {
			cirv.setProtocolCorpationId(cir.getProtocolCorpation().getId());
			cirv.setProtocolCorpationName(cir.getProtocolCorpation().getName());
		}
		return cirv;

	}

	public static List<CheckInRecordVo> convert(List<CheckInRecord> currentCheckInRecords) {
		if (currentCheckInRecords == null || currentCheckInRecords.isEmpty()) {
			return null;
		}
		List<CheckInRecordVo> data = new ArrayList<CheckInRecordVo>();
		for (CheckInRecord checkInRecord : currentCheckInRecords) {
			data.add(convert(checkInRecord));
		}
		return data;
	}
}
