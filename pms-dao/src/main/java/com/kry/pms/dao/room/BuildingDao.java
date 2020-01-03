package com.kry.pms.dao.room;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.room.Building;

public interface BuildingDao extends BaseDao<Building>{

	List<Building> findByHotelCode(String code);

	List<Building> findByHotelCodeAndStatus(String code, String normal);

}
