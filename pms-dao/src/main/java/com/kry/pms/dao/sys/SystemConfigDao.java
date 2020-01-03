package com.kry.pms.dao.sys;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.SystemConfig;

public interface SystemConfigDao extends BaseDao<SystemConfig>{

	SystemConfig findByHotelCodeAndKey(String hotelCode, String key);

}
