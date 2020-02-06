package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.ProtocolType;
import com.kry.pms.model.persistence.guest.VipType;

import java.util.List;

public interface ProtocolTypeDao extends BaseDao<ProtocolType>{

	List<ProtocolType> findByHotelCode(String hotelCode);

}
