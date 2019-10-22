package com.kry.pms.service.sys;

import java.time.LocalTime;

import com.kry.pms.model.persistence.sys.SystemConfig;
import com.kry.pms.service.BaseService;

public interface SystemConfigService extends BaseService<SystemConfig>{
	public SystemConfig getByHotelCodeAndKey(String hotelCode,String key);
	
	public LocalTime getAuditTime(String hotelCode);

	public LocalTime getCriticalTime(String hotelCode);
	
	public int getCurrentSeq(String key);

}