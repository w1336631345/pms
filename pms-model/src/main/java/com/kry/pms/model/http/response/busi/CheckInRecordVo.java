package com.kry.pms.model.http.response.busi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.kry.pms.model.persistence.busi.CheckInRecord;

import lombok.Data;

@Data
public class CheckInRecordVo {
	private String name;
	private LocalDateTime arriveTime;
	private LocalDateTime leaveTime;
	private String protocolCorpationName;
	private String protocolCorpationId;

	public CheckInRecordVo() {

	}

	public static CheckInRecordVo convert(CheckInRecord cir) {
		CheckInRecordVo cirv = new CheckInRecordVo();
		BeanUtils.copyProperties(cir, cirv);
		return cirv;

	}

	public static List<CheckInRecordVo> convert(List<CheckInRecord> currentCheckInRecords) {
		List<CheckInRecordVo> data = new ArrayList<CheckInRecordVo>();
		for (CheckInRecord checkInRecord : currentCheckInRecords) {
			data.add(convert(checkInRecord));
		}
		return data;
	}
}
