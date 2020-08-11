package com.kry.pms.service.marketing;

import java.util.List;
import java.util.Map;

import com.kry.pms.model.persistence.marketing.DistributionChannel;
import com.kry.pms.service.BaseService;

public interface DistributionChannelService extends BaseService<DistributionChannel>{

    void deleteTrue(String id);

    List<DistributionChannel> getAllByHotelCode(String currentHotleCode, int deletedFalse);

    List<Map<String, Object>> countRoom(String dateTime, String hotelCode);
}