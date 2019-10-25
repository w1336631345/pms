package com.kry.pms.service.busi;

import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.service.BaseService;

public interface DailyVerifyService extends BaseService<DailyVerify>{
	
	public void autoDailyVerify(String hotelCode);

}