package com.kry.pms.service.marketing;

import java.util.List;

import com.kry.pms.model.persistence.marketing.DistributionChannel;
import com.kry.pms.service.BaseService;

public interface DistributionChannelService extends BaseService<DistributionChannel>{

	List<DistributionChannel> getAllByHotelCode(String currentHotleCode, int deletedFalse);

}