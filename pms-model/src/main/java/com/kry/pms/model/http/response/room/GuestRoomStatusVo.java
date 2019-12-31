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
	private Boolean locked;// 锁定
	private Boolean willLeave;// 将离
	private Boolean willArrive;// 将到
	private Boolean hourRoom;// 钟点房
	private Boolean free;// 免费
	private Boolean personal;// 个人
	private Boolean group;// 团队
	private Boolean linkedRoom;// 联房
	private Boolean repairRoom;// 维修
	private Boolean overdued;// 欠费
	private Boolean ota;
}
