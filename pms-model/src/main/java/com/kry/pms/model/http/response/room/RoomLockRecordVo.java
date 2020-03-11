package com.kry.pms.model.http.response.room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.kry.pms.model.persistence.busi.RoomLockRecord;

import lombok.Data;

@Data
public class RoomLockRecordVo {
	private String roomNum;
	private String roomId;
	private String reason;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String remark;
	private String endToStatus;
	private Boolean autoReOpen;

	public static List<RoomLockRecordVo> convert(List<RoomLockRecord> lockRecords) {
		if (lockRecords == null || lockRecords.isEmpty()) {
			return null;
		}
		ArrayList<RoomLockRecordVo> data = new ArrayList<>();
		for (RoomLockRecord r : lockRecords) {
			data.add(convert(r));
		}
		return data;
	}

	public static RoomLockRecordVo convert(RoomLockRecord rlr) {
		if (rlr == null) {
			return null;
		}
		RoomLockRecordVo rlrv = new RoomLockRecordVo();
		BeanUtils.copyProperties(rlr, rlrv);
		rlrv.setRoomId(rlr.getGuestRoom().getId());
		rlrv.setRoomNum(rlr.getGuestRoom().getRoomNum());
		return rlrv;
	}

}
