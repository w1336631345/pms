package com.kry.pms.service.goods;

import com.kry.pms.model.persistence.goods.BosGoodsInfo;
import com.kry.pms.model.persistence.goods.BosGoodsType;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface BosGoodsInfoService extends BaseService<BosGoodsInfo>{

    void deleteByTypeId(String typeId);

    List<BosGoodsInfo> findByBosGoodsType(BosGoodsType bosGoodsType);

    List<BosGoodsInfo> findByBosGoodsTypeId(String bosGoodsTypeId);

    List<BosGoodsInfo> findByBosSiteId(String siteId);

    List<BosGoodsInfo> getByHotelCode(String hotelCode);

}