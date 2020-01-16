package com.kry.pms.model.http.response.room;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.kry.pms.model.persistence.room.GuestRoom;

import lombok.Data;

@Data
public class FloorVo implements Serializable{
	String id;
	String name;
	List<GuestRoomStatusVo> rooms;
}
