package com.kry.pms.model.http.request.busi;

import lombok.Data;

@Data
public class RoomAssignBo {
	String  checkInRecordId;
	Integer humanCountPreRoom;
	String roomId[];
}
