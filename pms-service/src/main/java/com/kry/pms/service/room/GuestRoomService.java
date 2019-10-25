package com.kry.pms.service.room;

import java.util.List;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.http.request.busi.RoomLockBo;
import com.kry.pms.model.persistence.busi.RoomLockRecord;
import com.kry.pms.model.persistence.room.Floor;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.BaseService;

public interface GuestRoomService extends BaseService<GuestRoom> {

	DtoResponse<List<GuestRoom>> batchAdd(GuestRoom guestRoom);

	DtoResponse<GuestRoom> addWithDto(GuestRoom guestRoom);

	List<GuestRoom> findByFloor(Floor floor);

	long findCountByFloor(Floor floor);

	DtoResponse<String> locked(RoomLockBo rlb);

}