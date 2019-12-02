package com.kry.pms.dao.marketing;

import java.util.List;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.marketing.MarketingSources;

public interface MarketingSourcesDao extends BaseDao<MarketingSources>{

	List<MarketingSources> findByHotelCodeAndDeleted(String currentHotleCode, int deleted);

}
