package com.kry.pms.service.room;

import java.util.List;

import com.kry.pms.model.persistence.room.RoomTag;
import com.kry.pms.service.BaseService;

public interface RoomTagService extends BaseService<RoomTag>{

	List<RoomTag> getAllByHotelCode(String currentHotleCode, int deletedFalse);

}