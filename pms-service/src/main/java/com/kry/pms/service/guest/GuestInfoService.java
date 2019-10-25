package com.kry.pms.service.guest;

import com.kry.pms.model.persistence.guest.GuestInfo;
import com.kry.pms.service.BaseService;

public interface GuestInfoService extends BaseService<GuestInfo>{
	public GuestInfo findByIdCardNum(String idCardNum);

}