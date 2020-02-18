package com.kry.pms.service.room;

import java.util.List;

import com.kry.pms.model.persistence.room.RoomType;
import com.kry.pms.service.BaseService;

public interface RoomTypeService extends BaseService<RoomType>{

	List<RoomType> getAllByHotelCode(String code, int deleted);

	void plusRoomQuantity(RoomType roomType, int size);

}