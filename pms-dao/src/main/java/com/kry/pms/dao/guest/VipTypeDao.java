package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.VipType;

import java.util.List;

public interface VipTypeDao extends BaseDao<VipType>{

	List<VipType> findByHotelCode(String hotelCode);



}
