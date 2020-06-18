package com.kry.pms.service.goods;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.goods.BosBusinessSite;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface BosBusinessSiteService extends BaseService<BosBusinessSite>{

    List<BosBusinessSite> getByHotelCode(String hotelCode);

    HttpResponse deleteAll(String id, String deleteAll);
}