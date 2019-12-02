package com.kry.pms.dao.marketing;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.marketing.DistributionChannel;

public interface DistributionChannelDao extends BaseDao<DistributionChannel>{

	List<DistributionChannel> findByHotelCodeAndDeleted(String currentHotleCode, int deleted);

}
