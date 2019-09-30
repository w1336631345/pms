package com.kry.pms.service.room;

import java.util.List;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.persistence.room.Floor;
import com.kry.pms.service.BaseService;

public interface FloorService extends BaseService<Floor>{

	DtoResponse<Floor> deleteWithCheck(String id);
	
	List<Floor> findByBuildingId(String buildingId,int deleted);

}