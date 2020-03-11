package com.kry.pms.model.http.response.room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.kry.pms.model.persistence.busi.RoomRepairRecord;

import lombok.Data;

@Data
public class RoomRepairRecordVo {
	private String roomNum;
	private String roomId;
	private String reason;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String remark;
	private String endToStatus;
	private Boolean autoReOpen;

	public static List<RoomRepairRecordVo> convert(List<RoomRepairRecord> lockRecords) {
		if (lockRecords == null || lockRecords.isEmpty()) {
			return null;
		}
		ArrayList<RoomRepairRecordVo> data = new ArrayList<>();
		for (RoomRepairRecord r : lockRecords) {
			data.add(convert(r));
		}
		return data;
	}

	public static RoomRepairRecordVo convert(RoomRepairRecord rlr) {
		if (rlr == null) {
			return null;
		}
		RoomRepairRecordVo rlrv = new RoomRepairRecordVo();
		BeanUtils.copyProperties(rlr, rlrv);
		rlrv.setRoomId(rlr.getGuestRoom().getId());
		rlrv.setRoomNum(rlr.getGuestRoom().getRoomNum());
		return rlrv;
	}

}
