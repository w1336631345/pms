package com.kry.pms.model.http.response.room;

import java.io.Serializable;

import com.kry.pms.model.persistence.room.GuestRoom;

import lombok.Data;
@Data
public class GuestRoomVo implements Serializable {
	private String id;
	private String roomNum;
	private String roomTypeName;
	private String mainPicture;
	private Integer bedCount;
	private Integer occupantCapacity;

	public GuestRoomVo(GuestRoom gr) {
		this.id = gr.getId();
		this.roomNum = gr.getRoomNum();
		this.mainPicture = gr.getMainPicture();
		this.bedCount = gr.getBedCount();
		this.occupantCapacity = gr.getOccupantCapacity();
	}
}
