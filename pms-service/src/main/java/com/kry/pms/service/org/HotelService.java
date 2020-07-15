package com.kry.pms.service.org;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.http.response.org.HotelInfoVo;
import com.kry.pms.model.persistence.org.Hotel;
import com.kry.pms.service.BaseService;

import java.util.List;
import java.util.Map;

public interface HotelService extends BaseService<Hotel>{

    Hotel getByHotelCode(String code);

	HotelInfoVo getHotelInfo(String currentHotleCode);

    List<Map<String, Object>> getByUnionId(String unionId);

    HttpResponse getByHotelCodeAndDeleted(String currentHotleCode);
}