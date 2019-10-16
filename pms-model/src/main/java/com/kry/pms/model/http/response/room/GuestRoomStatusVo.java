package com.kry.pms.model.http.response.room;

import java.io.Serializable;

import lombok.Data;
@Data
public class GuestRoomStatusVo implements Serializable {
	private String roomTypeName;
	private String roomStatusId;
	private String roomNum;
	private String summary;
	private String guestRoomId;
	private String roomStatus;
}
