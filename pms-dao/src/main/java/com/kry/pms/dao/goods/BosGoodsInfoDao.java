package com.kry.pms.dao.goods;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.goods.BosGoodsInfo;
import com.kry.pms.model.persistence.goods.BosGoodsType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BosGoodsInfoDao extends BaseDao<BosGoodsInfo>{


    List<BosGoodsInfo> findByHotelCode(String code);

    @Modifying
    @Query(nativeQuery = true, value = " DELETE FROM t_bos_goods_info where bos_goods_type_id = ?1 ")
    void deleteByTypeId(String typeId);

    List<BosGoodsInfo> findByBosGoodsType(BosGoodsType bosGoodsType);

    @Query(nativeQuery = true, value = " select * from t_bos_goods_info where bos_goods_type_id = ?1 ")
    List<BosGoodsInfo> findByBosGoodsTypeId(String bosGoodsTypeId);

    @Query(nativeQuery = true, value = " select tbi.* from t_bos_goods_info tbi, t_bos_goods_type tbt, t_bos_business_site tbs \n" +
            " where tbi.bos_goods_type_id = tbt.id and tbt.bos_business_site_id = tbs.id \n" +
            " and tbs.id = ?1 ")
    List<BosGoodsInfo> findByBosSiteId(String bosGoodsTypeId);

}
