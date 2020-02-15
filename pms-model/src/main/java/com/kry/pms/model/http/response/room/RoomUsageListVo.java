package com.kry.pms.model.http.response.room;

import java.time.LocalDateTime;

import com.kry.pms.model.persistence.room.RoomUsage;

import lombok.Data;

@Data
public class RoomUsageListVo {
	protected String id;
	private String guestRoomId; //房间id
	private String roomNum;//房间号
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String roomTypeName;//房间类型
	private String usageStatus;//使用状态
	private String businesskey;
	private String businessInfo;
	private Long duration;

	public RoomUsageListVo(String id, String roomNum, LocalDateTime startDateTime, LocalDateTime endDateTime,
			String roomTypeName, String usageStatus, String businesskey, String businessInfo, Long duration,String guestRoomId) {
		super();
		this.id = id;
		this.roomNum = roomNum;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.roomTypeName = roomTypeName;
		this.usageStatus = usageStatus;
		this.businesskey = businesskey;
		this.businessInfo = businessInfo;
		this.duration = duration;
		this.guestRoomId = guestRoomId;
	}

	public RoomUsageListVo(RoomUsage ru) {
		// TODO Auto-generated constructor stub
	}

}
