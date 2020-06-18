package com.kry.pms.service.goods;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.goods.BosBusinessSite;
import com.kry.pms.model.persistence.goods.BosGoodsType;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface BosGoodsTypeService extends BaseService<BosGoodsType>{

    List<BosGoodsType> findByBosBusinessSite(BosBusinessSite bosBusinessSite);

    List<BosGoodsType> findByBosBusinessSiteId(String bosBusinessSiteId);

    List<BosGoodsType> getByHotelCode(String hotelCode);

    void deleteBySiteId(String bosBusinessSiteId);

    HttpResponse deleteAll(String id, String deleteAll);
}