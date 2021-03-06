package com.kry.pms.service.room;

import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomStatusQuantity;
import com.kry.pms.service.BaseService;

public interface RoomStatusQuantityService extends BaseService<RoomStatusQuantity>{


	public void checkIn(GuestRoom gr, String oldStatus);

	public void transformRoomStatusQuantity(String hotelCode, String oldStatus, String newStatus, int quantity);
	
}