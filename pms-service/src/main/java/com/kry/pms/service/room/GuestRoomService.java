package com.kry.pms.service.room;

import java.util.List;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.request.busi.GuestRoomOperation;
import com.kry.pms.model.persistence.room.Floor;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.service.BaseService;

import javax.transaction.Transactional;

public interface GuestRoomService extends BaseService<GuestRoom> {

    @Transactional
    GuestRoom addRoomAndResources(GuestRoom guestRoom);

    DtoResponse<List<GuestRoom>> batchAdd(GuestRoom guestRoom);

//	void addRoomRelated(GuestRoom gr);

    @Transactional
    DtoResponse<GuestRoom> removeRoomRelated(String id);

    @Transactional
    HttpResponse updateRoom(GuestRoom guestRoom);

    DtoResponse<GuestRoom> addWithDto(GuestRoom guestRoom);

	List<GuestRoom> findByFloor(Floor floor);

	long findCountByFloor(Floor floor);

	DtoResponse<String> statusOperation(GuestRoomOperation operation);

}