package com.kry.pms.dao.sys;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.SystemConfig;

public interface SystemConfigDao extends BaseDao<SystemConfig>{

	SystemConfig findByHotelCodeAndKey(String hotelCode, String key);

	List<SystemConfig> findByHotelCodeAndUseOnWeb(String hotelCode, boolean b);

}
