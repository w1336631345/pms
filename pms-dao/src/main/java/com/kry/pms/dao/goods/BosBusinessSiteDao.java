package com.kry.pms.dao.goods;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.goods.BosBusinessSite;

import java.util.List;

public interface BosBusinessSiteDao extends BaseDao<BosBusinessSite>{


    List<BosBusinessSite> findByHotelCode(String code);
}
