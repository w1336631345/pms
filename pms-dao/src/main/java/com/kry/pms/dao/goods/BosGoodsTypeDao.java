package com.kry.pms.dao.goods;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.goods.BosBusinessSite;
import com.kry.pms.model.persistence.goods.BosGoodsType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BosGoodsTypeDao extends BaseDao<BosGoodsType>{


    List<BosGoodsType> findByHotelCode(String code);

    @Modifying
    @Query(nativeQuery = true, value = " DELETE FROM t_bos_goods_type where bos_business_site_id = ?1 ")
    void deleteBySiteId(String siteId);

    List<BosGoodsType> findByBosBusinessSite(BosBusinessSite bosBusinessSite);

    @Query(nativeQuery = true, value = " select * from t_bos_goods_type where bos_business_site_id = ?1 ")
    List<BosGoodsType> findByBosBusinessSiteId(String bosBusinessSiteId);
}
