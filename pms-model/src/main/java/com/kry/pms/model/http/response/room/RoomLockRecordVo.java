package com.kry.pms.model.http.response.room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.model.persistence.dict.RoomLockReason;

import lombok.Data;

@Data
public class RoomLockRecordVo {
	private String roomNum;
	private String roomId;
	private RoomLockReason reason;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String remark;
	private String endToStatus;
	private Boolean autoReOpen;

	public static List<RoomLockRecordVo> convert(List<RoomLockRecord> lockRecords) {
		ArrayList<RoomLockRecordVo> data = new ArrayList<>();
		for (RoomLockRecord r : lockRecords) {
			data.add(convert(r));
		}
		return data;
	}

	public static RoomLockRecordVo convert(RoomLockRecord rlr) {
		RoomLockRecordVo rlrv = new RoomLockRecordVo();
		BeanUtils.copyProperties(rlr, rlrv);
		rlrv.setRoomId(rlr.getGuestRoom().getId());
		rlrv.setRoomNum(rlr.getGuestRoom().getRoomNum());
		return null;
	}

}
