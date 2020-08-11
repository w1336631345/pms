package com.kry.pms.service.marketing;

import java.util.List;
import java.util.Map;

import com.kry.pms.model.persistence.marketing.MarketingSources;
import com.kry.pms.service.BaseService;

public interface MarketingSourcesService extends BaseService<MarketingSources>{

	List<MarketingSources> getAllByHotelCode(String currentHotleCode, int deletedFalse);

    List<MarketingSources> getByHotelCodeAndIsUsed(String currentHotleCode, String isUsed, int deleted);

    List<Map<String, Object>> countRoom(String dateTime, String hotelCode);
}