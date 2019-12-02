package com.kry.pms.service.org;

import com.kry.pms.model.http.response.org.HotelInfoVo;
import com.kry.pms.model.persistence.org.Hotel;
import com.kry.pms.service.BaseService;

public interface HotelService extends BaseService<Hotel>{

    Hotel getByHotelCode(String code);

	HotelInfoVo getHotelInfo(String currentHotleCode);
}