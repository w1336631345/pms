package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.VipRoomType;

import java.util.List;

public interface VipRoomTypeDao extends BaseDao<VipRoomType>{

	List<VipRoomType> findByHotelCode(String hotelCode);



}
