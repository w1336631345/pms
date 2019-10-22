package com.kry.pms.model.http.request.busi;

import lombok.Data;

@Data
public class RoomAssignBo {
	String bookItemId;
	String bookId;
	Integer humanCountPreRoom;
	String roomId[];
}
